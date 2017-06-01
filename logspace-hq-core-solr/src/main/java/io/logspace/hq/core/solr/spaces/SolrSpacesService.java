/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr.spaces;

import static io.logspace.hq.core.solr.ConfigFieldConstants.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.logspace.hq.core.api.spaces.SpacesService;
import io.logspace.hq.core.solr.ConfigFieldConstants;
import io.logspace.hq.core.solr.ConfigQualifier;
import io.logspace.hq.core.solr.utils.SolrDocumentHelper;
import io.logspace.hq.rest.api.DataRetrievalException;
import io.logspace.hq.rest.api.DataStorageException;

@Named
public class SolrSpacesService implements SpacesService {

    private static final String CONFIG_TYPE = "space";

    @Inject
    @ConfigQualifier
    private SolrClient solrClient;

    private final Map<String, String> spaces = new ConcurrentHashMap<>();

    private boolean update;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PreDestroy
    public void destroy() {
        this.update = false;
        this.wakeUp();
    }

    @Override
    public String getSpaceForAuthenticationToken(String authenticationToken) {
        return this.spaces.get(authenticationToken);
    }

    @PostConstruct
    public void initialize() {
        try {
            this.loadSpacesFromSolr();
        } catch (SolrServerException | IOException e) {
            throw new DataRetrievalException("Could not load spaces from Solr", e);
        }

        this.update = true;
        new Thread(this::updateSpaces, "Update-Spaces").start();
    }

    @Override
    public void setAuthenticationTokens(String space, String... authenticationTokens) {
        try {
            SolrInputDocument solrInputDocument = new SolrInputDocument();

            solrInputDocument.setField(FIELD_ID, "space_" + space);
            solrInputDocument.setField(FIELD_TYPE, CONFIG_TYPE);
            solrInputDocument.setField(FIELD_NAME, space);
            solrInputDocument.setField(FIELD_TIMESTAMP, new Date());
            solrInputDocument.setField(FIELD_CONTENT, StringUtils.join(authenticationTokens, ","));

            this.solrClient.add(solrInputDocument);

            for (String eachToken : authenticationTokens) {
                this.spaces.put(eachToken, space);
            }
        } catch (SolrServerException | IOException e) {
            throw new DataStorageException("Could not store authentication tokens.", e);
        }
    }

    private void loadSpacesFromSolr() throws SolrServerException, IOException {
        this.logger.debug("Loading spaces from Config Solr.");
        Set<String> activeTokens = new HashSet<>();

        SolrQuery solrQuery = new SolrQuery("*:*");
        solrQuery.setFilterQueries(ConfigFieldConstants.FIELD_TYPE + ":" + CONFIG_TYPE);
        solrQuery.setRows(Integer.MAX_VALUE);

        QueryResponse queryResponse = this.solrClient.query(solrQuery);
        for (SolrDocument eachSolrDocument : queryResponse.getResults()) {
            String spaceName = SolrDocumentHelper.getString(eachSolrDocument, FIELD_NAME);
            String tokenString = SolrDocumentHelper.getString(eachSolrDocument, FIELD_CONTENT);
            String[] tokens = tokenString.split("\\s*,\\s*");

            for (String eachToken : tokens) {
                this.spaces.put(eachToken, spaceName);
                activeTokens.add(eachToken);
            }
        }

        this.spaces.keySet().retainAll(activeTokens);
    }

    private synchronized void sleep() {
        if (this.update) {
            try {
                this.wait(60000);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    private void updateSpaces() {
        while (this.update) {
            this.sleep();

            try {
                this.loadSpacesFromSolr();
            } catch (SolrServerException | IOException e) {
                this.logger.error("Could not load spaces from Solr.", e);
            }
        }
    }

    private synchronized void wakeUp() {
        this.notifyAll();
    }
}
