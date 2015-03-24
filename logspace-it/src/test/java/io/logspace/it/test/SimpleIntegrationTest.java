/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.it.test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.logspace.agent.api.AbstractAgent;
import io.logspace.agent.api.event.DefaultEventBuilder;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;
import io.logspace.agent.impl.AgentControllerProvider;
import io.logspace.agent.impl.HqAgentController;
import io.logspace.it.AbstractLogspaceTest;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.junit.Test;

public class SimpleIntegrationTest extends AbstractLogspaceTest {

    @Test
    public void testMissingAgent() {
        assertEquals(0, this.commitAndGetSolrDocumentCount("*:*"));

        HqAgentController.install("1", "http://localhost:4567");
        this.waitFor(5, SECONDS);
        AgentControllerProvider.shutdown();

        assertEquals(0, this.commitAndGetSolrDocumentCount("*:*"));
    }

    @Test
    public void testMissingOrder() {
        assertEquals(0, this.commitAndGetSolrDocumentCount("*:*"));

        HqAgentController.install("2", "http://localhost:4567");
        this.waitFor(5, SECONDS);
        AgentControllerProvider.shutdown();

        assertEquals(0, this.commitAndGetSolrDocumentCount("*:*"));
    }

    @Test
    public void testSimpleAgent() throws SolrServerException, IOException {
        assertEquals(0, this.commitAndGetSolrDocumentCount("*:*"));

        HqAgentController.install("1", "http://localhost:4567");
        TestAgent testAgent = new TestAgent();
        this.waitFor(5, SECONDS);
        AgentControllerProvider.shutdown();

        assertTrue(testAgent.getEventCount() > 0);
        assertEquals(testAgent.getEventCount(), this.commitAndGetSolrDocumentCount("*:*"));

        SolrQuery solrQuery = new SolrQuery("*:*");
        solrQuery.addSort("global_id", ORDER.asc);

        QueryResponse queryResponse = this.getSolrServer().query(solrQuery);

        int documentCount = 0;
        for (SolrDocument eachDocument : queryResponse.getResults()) {
            assertEquals(String.valueOf(++documentCount), eachDocument.getFirstValue("global_id"));
        }

        this.getSolrServer().deleteByQuery("*:*");
        this.commitSolr();
    }

    public static class TestAgent extends AbstractAgent {

        private int eventCount;

        public TestAgent() {
            super();

            this.setId("TEST-AGENT-001");
            this.setType("TEST-AGENT");
            this.updateCapabilities(TriggerType.Off, TriggerType.Cron);

            this.setAgentController(AgentControllerProvider.getAgentController());
        }

        @Override
        public void execute(AgentOrder agentOrder) {
            Event event = new DefaultEventBuilder().setGlobalEventId(String.valueOf(++this.eventCount)).toEvent();
            this.sendEvent(event);
        }

        public int getEventCount() {
            return this.eventCount;
        }
    }
}