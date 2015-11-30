/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.journal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.logspace.agent.api.AgentControllerProvider;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.test.TestAgentController;

public class JournalAgentTest {

    @Test
    public void test() {
        TestAgentController.installIfRequired("./target/test-events.json");

        assertEquals("Expected no Events at the beginning.", 0, TestAgentController.getCollectedEventCount());

        JournalAgent.create("test-journal").triggerEvent("test-category", "test-message");
        assertEquals("Expected one Journal Event.", 1, TestAgentController.getCollectedEventCount());

        Event event = TestAgentController.getCollectedEvent(0);
        assertEquals("test-category", TestAgentController.getProperty(event.getStringProperties(), "category"));
        assertEquals("test-message", TestAgentController.getProperty(event.getStringProperties(), "message"));

        AgentControllerProvider.shutdown();
    }
}
