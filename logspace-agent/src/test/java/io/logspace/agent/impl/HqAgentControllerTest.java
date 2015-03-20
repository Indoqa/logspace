/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.impl;

import io.logspace.agent.api.AbstractAgent;
import io.logspace.agent.api.event.DefaultEventBuilder;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class HqAgentControllerTest {

    @BeforeClass
    public static void beforeClass() {
        HqAgentController.install("1", "http://localhost:4567");
    }

    @Test
    public void test() {
        TestAgent testAgent = new TestAgent();

        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // do nothing
            }
        }

        System.out.println("Finished");
    }

    public static class TestAgent extends AbstractAgent {

        public TestAgent() {
            super();

            this.setId("TEST-AGENT-001");
            this.setType("TEST-AGENT");
            this.updateCapabilities(TriggerType.Off, TriggerType.Cron);

            this.setAgentController(AgentControllerProvider.getAgentController());
        }

        @Override
        public void execute(AgentOrder agentOrder) {
            System.out.println(System.currentTimeMillis() + ": Creating event");
            Event event = new DefaultEventBuilder().setGlobalEventId(UUID.randomUUID().toString()).toEvent();
            this.sendEvent(event);
        }
    }
}
