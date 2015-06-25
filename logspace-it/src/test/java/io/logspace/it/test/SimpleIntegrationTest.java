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
import io.logspace.agent.api.AbstractSchedulerAgent;
import io.logspace.agent.api.AgentControllerProvider;
import io.logspace.agent.api.event.DefaultEventBuilder;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.hq.HqAgentController;
import io.logspace.it.AbstractLogspaceTest;
import io.logspace.it.InfrastructureRule;

import java.util.UUID;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.junit.Before;
import org.junit.Test;

public class SimpleIntegrationTest extends AbstractLogspaceTest {

    private static final String HQ_URL = "http://localhost:" + InfrastructureRule.TEST_PORT;
    private static final String QUEUE_FILE = "./target/queue-file.dat";
    private static final String SPACE_TOKEN = "test";

    @Before
    public void before() {
        this.deleteByQuery("*:*");
    }

    @Test
    public void testMissingAgent() {
        assertEquals(0, this.commitAndGetSolrDocumentCount("*:*"));

        HqAgentController.install("1", HQ_URL, QUEUE_FILE, SPACE_TOKEN);
        AgentControllerProvider.getAgentController();

        this.waitFor(5, SECONDS);
        AgentControllerProvider.shutdown();

        assertEquals(0, this.commitAndGetSolrDocumentCount("*:*"));
    }

    @Test
    public void testMissingOrder() {
        assertEquals(0, this.commitAndGetSolrDocumentCount("*:*"));

        HqAgentController.install("2", HQ_URL, QUEUE_FILE, SPACE_TOKEN);
        AgentControllerProvider.getAgentController();

        this.waitFor(5, SECONDS);
        AgentControllerProvider.shutdown();

        assertEquals(0, this.commitAndGetSolrDocumentCount("*:*"));
    }

    @Test
    public void testSimpleAgent() {
        assertEquals(0, this.commitAndGetSolrDocumentCount("*:*"));

        HqAgentController.install("1", HQ_URL, QUEUE_FILE, SPACE_TOKEN);
        TestAgent testAgent = new TestAgent();
        this.waitFor(5, SECONDS);
        AgentControllerProvider.shutdown();

        assertTrue(testAgent.getEventCount() > 0);
        assertEquals(testAgent.getEventCount(), this.commitAndGetSolrDocumentCount("*:*"));

        SolrQuery solrQuery = new SolrQuery("*:*");
        solrQuery.addSort("global_id", ORDER.asc);

        QueryResponse queryResponse = this.querySolr(solrQuery);

        int documentCount = 0;
        for (SolrDocument eachDocument : queryResponse.getResults()) {
            assertEquals(String.valueOf(++documentCount), eachDocument.getFirstValue("global_id"));
        }
    }

    @Test
    public void testUnrecognizedSpaceToken() {
        assertEquals(0, this.commitAndGetSolrDocumentCount("*:*"));

        HqAgentController.install("1", HQ_URL, QUEUE_FILE, UUID.randomUUID().toString());
        TestAgent testAgent = new TestAgent();
        this.waitFor(5, SECONDS);
        AgentControllerProvider.shutdown();

        assertEquals(0, testAgent.getEventCount());
        assertEquals(0, this.commitAndGetSolrDocumentCount("*:*"));
    }

    public static class TestAgent extends AbstractSchedulerAgent {

        private int eventCount;

        public TestAgent() {
            super("TEST-AGENT-001", "TEST-AGENT");
        }

        @Override
        public void execute(AgentOrder agentOrder) {
            Event event = new DefaultEventBuilder(this.getId(), "TEST").setGlobalEventId(String.valueOf(++this.eventCount)).toEvent();
            this.sendEvent(event);
        }

        public int getEventCount() {
            return this.eventCount;
        }
    }
}