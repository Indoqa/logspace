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

import io.logspace.hq.core.api.orders.OrderService;
import io.logspace.hq.core.api.report.ReportService;
import io.logspace.hq.core.api.spaces.SpacesService;

@FixMethodOrder(MethodSorters.JVM)
public abstract class AbstractLogspaceTest {

    @ClassRule
    public static final InfrastructureRule INFRASTRUCTURE_RULE = new InfrastructureRule();

    protected final long commitAndGetSolrDocumentCount(String query) {
        this.commitEventSolr();
        return this.getSolrDocumentCount(query);
    }

    protected final void commitConfigSolr() {
        try {
            this.getConfigSolrClient().commit(true, true, true);
        } catch (Exception e) {
            throw new LogspaceTestException("Failed to commit Config Solr.", e);
        }
    }

    protected final void commitEventSolr() {
        try {
            this.getEventSolrClient().commit(true, true, true);
        } catch (Exception e) {
            throw new LogspaceTestException("Failed to commit Event Solr.", e);
        }
    }

    protected final void deleteByQuery(String query) {
        try {
            this.getEventSolrClient().deleteByQuery(query);
            this.commitEventSolr();
        } catch (Exception e) {
            throw new LogspaceTestException("Failed to delete with query '" + query + "'.", e);
        }
    }

    protected SolrClient getConfigSolrClient() {
        return INFRASTRUCTURE_RULE.getConfigSolrClient();
    }

    protected final SolrClient getEventSolrClient() {
        return INFRASTRUCTURE_RULE.getEventSolrClient();
    }

    protected final OrderService getOrderService() {
        return INFRASTRUCTURE_RULE.getOrderService();
    }

    protected final ReportService getReportService() {
        return INFRASTRUCTURE_RULE.getReportService();
    }

    protected final long getSolrDocumentCount(String query) {
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.setRows(0);

        try {
            QueryResponse queryResponse = this.getEventSolrClient().query(solrQuery);
            return queryResponse.getResults().getNumFound();
        } catch (Exception e) {
            throw new LogspaceTestException("Failed to retrieve Solr document count for query '" + query + "'.", e);
        }
    }

    protected final SpacesService getSpacesService() {
        return INFRASTRUCTURE_RULE.getSpacesService();
    }

    protected final QueryResponse querySolr(SolrParams solrQuery) {
        try {
            return this.getEventSolrClient().query(solrQuery);
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
