package io.logspace.agent.impl;

import io.logspace.agent.api.Agent;
import io.logspace.agent.api.AgentController;
import io.logspace.agent.api.event.ImmutableEvent;
import io.logspace.agent.api.event.Optional;
import io.logspace.agent.api.eventrequest.HqEventRequest;
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

    public static class TestAgent implements Agent {

        private AgentController agentController = AgentControllerProvider.getAgentController();

        @Override
        public AgentCapabilities provideCapabilities() {
            return null;
        }

        @Override
        public void receiveEventRequest(HqEventRequest eventRequest) {
        }

        @Override
        public void setAgentController(AgentController agentController) {
        }

        public void triggerEvent() {
            this.agentController.send(new ImmutableEvent(Optional.of("TEST"), Optional.<String> empty(), Optional.<String> empty()));
        }
    }
}