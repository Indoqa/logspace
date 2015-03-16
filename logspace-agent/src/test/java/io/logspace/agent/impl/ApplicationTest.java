/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.impl;

import io.logspace.agent.api.AbstractAgent;
import io.logspace.agent.api.AgentController;
import io.logspace.agent.api.event.ImmutableEvent;
import io.logspace.agent.api.event.Optional;
import io.logspace.agent.api.order.AgentCapabilities;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ApplicationTest {

    @AfterClass
    public static void afterClass() {
        AgentControllerProvider.flush();
    }

    @BeforeClass
    public static void beforeClass() {
        TestAgentController.installIfRequired("./target/logspace-test.json");
    }

    @Test
    public void executeTest() {
        TestAgent testAgent = new TestAgent();

        for (int i = 0; i < 10; i++) {
            testAgent.triggerEvent();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }

    public static class TestAgent extends AbstractAgent {

        private AgentController agentController = AgentControllerProvider.getAgentController();

        @Override
        public AgentCapabilities getCapabilities() {
            return null;
        }

        public void triggerEvent() {
            this.agentController.send(new ImmutableEvent(Optional.of("TEST"), Optional.<String> empty(), Optional.<String> empty()));
        }
    }
}