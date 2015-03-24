/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.it;

import java.util.concurrent.TimeUnit;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractLogspaceTest {

    @ClassRule
    public static InfrastructureRule infrastructureRule = new InfrastructureRule();

    protected final void commitSolrDocument() {
        try {
            this.getSolrServer().commit();
        } catch (Exception e) {
            throw new LogspaceTestException("Failed to commit Solr server.", e);
        }
    }

    protected final long getSolrDocumentCount(String query) {
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.setRows(0);

        try {
            QueryResponse queryResponse = this.getSolrServer().query(solrQuery);
            return queryResponse.getResults().getNumFound();
        } catch (Exception e) {
            throw new LogspaceTestException("Failed to retrieve Solr document count for query '" + query + "'.", e);
        }
    }

    protected final SolrServer getSolrServer() {
        return infrastructureRule.getSolrServer();
    }

    protected final void waitFor(long duration, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(duration);
        } catch (InterruptedException e) {
            // do nothing
        }
    }
}