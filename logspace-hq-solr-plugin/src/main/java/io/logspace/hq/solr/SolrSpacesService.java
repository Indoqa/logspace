/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import static io.logspace.hq.solr.ConfigFieldConstants.*;

import java.io.IOException;
import java.util.*;

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
import org.springframework.beans.factory.annotation.Qualifier;

import io.logspace.hq.core.api.spaces.SpacesService;
import io.logspace.hq.rest.api.DataStorageException;
import io.logspace.hq.solr.utils.SolrDocumentHelper;

@Named
public class SolrSpacesService implements SpacesService {

    private static final String CONFIG_TYPE = "space";

    private final Map<String, String> spaces = new HashMap<String, String>();

    private boolean update;

    @Inject
    @Qualifier("config-solr-client")
    private SolrClient solrClient;

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

    private void loadSpacesFromSolr() {
        try {
            Set<String> activeTokens = new HashSet<>();

            SolrQuery solrQuery = new SolrQuery();
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
        } catch (SolrServerException | IOException e) {
            this.logger.error("Could not load spaces from Solr.", e);
        }
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
            this.loadSpacesFromSolr();

            this.sleep();
        }
    }

    private synchronized void wakeUp() {
        this.notifyAll();
    }
}
