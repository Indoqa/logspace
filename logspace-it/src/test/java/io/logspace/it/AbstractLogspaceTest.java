/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.it;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM)
public abstract class AbstractLogspaceTest {

    @ClassRule
    public static InfrastructureRule infrastructureRule = new InfrastructureRule();

    protected final long commitAndGetSolrDocumentCount(String query) {
        this.commitSolr();
        return this.getSolrDocumentCount(query);
    }

    protected final void commitSolr() {
        try {
            this.getSolrClient().commit();
        } catch (Exception e) {
            throw new LogspaceTestException("Failed to commit Solr server.", e);
        }
    }

    protected final void deleteByQuery(String query) {
        try {
            this.getSolrClient().deleteByQuery(query);
            this.commitSolr();
        } catch (Exception e) {
            throw new LogspaceTestException("Failed to delete with query '" + query + "'.", e);
        }
    }

    protected final SolrClient getSolrClient() {
        return infrastructureRule.getSolrClient();
    }

    protected final long getSolrDocumentCount(String query) {
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.setRows(0);

        try {
            QueryResponse queryResponse = this.getSolrClient().query(solrQuery);
            return queryResponse.getResults().getNumFound();
        } catch (Exception e) {
            throw new LogspaceTestException("Failed to retrieve Solr document count for query '" + query + "'.", e);
        }
    }

    protected final QueryResponse querySolr(SolrParams solrQuery) {
        try {
            return this.getSolrClient().query(solrQuery);
        } catch (SolrServerException | IOException e) {
            throw new LogspaceTestException("Could not query documents from solr.", e);
        }
    }

    protected final void waitFor(long duration, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(duration);
        } catch (InterruptedException e) {
            // do nothing
        }
    }
}