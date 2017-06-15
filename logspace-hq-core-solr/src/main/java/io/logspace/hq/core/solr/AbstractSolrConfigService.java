/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr;

import java.io.IOException;
import java.util.Collections;

import javax.inject.Inject;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSolrConfigService {

    @Inject
    @ConfigQualifier
    protected SolrClient solrClient;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FieldDefinitions fieldDefinitions = this.createFieldDefinitions();

    protected static Object asFieldUpdate(Object value) {
        return Collections.singletonMap("set", value);
    }

    protected static void documentMustExist(SolrInputDocument solrInputDocument) {
        solrInputDocument.setField(ConfigFieldConstants.FIELD_VERSION, 1);
    }

    protected static void documentMustNotExist(SolrInputDocument solrInputDocument) {
        solrInputDocument.setField(ConfigFieldConstants.FIELD_VERSION, -1);
    }

    public FieldDefinitions getFieldDefinitions() {
        return this.fieldDefinitions;
    }

    protected FieldDefinitions createFieldDefinitions() {
        return FieldDefinitions.empty();
    }

    protected void makeChangesVisible() throws SolrServerException, IOException {
        this.solrClient.commit(false, true, true);
    }

    protected SolrDocument realTimeGet(String id) throws SolrServerException, IOException {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setRequestHandler("/get");
        solrQuery.set("id", id);

        QueryResponse response = this.solrClient.query(solrQuery);
        return (SolrDocument) response.getResponse().get("doc");
    }
}
