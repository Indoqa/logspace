/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.it.test;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import io.logspace.agent.api.AbstractApplicationAgent;
import io.logspace.agent.api.AgentControllerProvider;
import io.logspace.agent.api.event.DefaultEventBuilder;
import io.logspace.agent.hq.HqAgentController;
import io.logspace.it.AbstractLogspaceTest;
import io.logspace.it.InfrastructureRule;

@Ignore
public class SimplePerformanceTest extends AbstractLogspaceTest {

    private static final String HQ_URL = "http://localhost:" + InfrastructureRule.TEST_PORT;
    private static final String QUEUE_DIRECTORY = "./target/";
    private static final String SPACE_TOKEN = "test";
    private static final String MARKER = "marker";

    @Test
    public void testSimpleAgent() {
        assertEquals(0, this.commitAndGetSolrDocumentCount("*:*"));

        HqAgentController.install("1", HQ_URL, QUEUE_DIRECTORY, SPACE_TOKEN, MARKER);

        PerformanceTestAgent agent = new PerformanceTestAgent();

        long startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            agent.execute();
        }
        long duration = System.nanoTime() - startTime;

        AgentControllerProvider.shutdown();

        assertTrue(agent.getEventCount() > 0);
        assertEquals(agent.getEventCount(), this.commitAndGetSolrDocumentCount("*:*"));

        System.out.println(1000000000d * agent.getEventCount() / duration + " events/s");
    }

    private static class PerformanceTestAgent extends AbstractApplicationAgent {

        private int eventCount;

        public PerformanceTestAgent() {
            super("Performance-Test-Agent-001", "Performance-Test-Agent");
        }

        public void execute() {
            DefaultEventBuilder builder = new DefaultEventBuilder(this.getEventBuilderData());
            builder.setGlobalEventId(String.valueOf(++this.eventCount));
            this.sendEvent(builder.toEvent());
        }

        public int getEventCount() {
            return this.eventCount;
        }
    }

}
