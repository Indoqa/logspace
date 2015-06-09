/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.journal;

import static org.junit.Assert.assertEquals;
import io.logspace.agent.api.AgentControllerProvider;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.agent.test.TestAgentController;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class JournalAgentTest {

    private static String getProperty(Collection<? extends EventProperty<String>> eventProperties, String name) {
        for (EventProperty<String> eachEventProperty : eventProperties) {
            if (eachEventProperty.getKey().equals(name)) {
                return eachEventProperty.getValue();
            }
        }

        return null;
    }

    @Test
    public void test() {
        TestAgentController.installIfRequired("./target/test-events.json");
        TestAgentController agentController = (TestAgentController) AgentControllerProvider.getAgentController();

        List<Event> collectedEvents = agentController.getCollectedEvents();
        assertEquals(0, collectedEvents.size());

        JournalAgent.create("test-journal").triggerEvent("test-category", "test-message");

        collectedEvents = agentController.getCollectedEvents();
        assertEquals("Expected one journal event.", 1, collectedEvents.size());
        Event event = collectedEvents.get(0);

        assertEquals("test-category", getProperty(event.getStringProperties(), "category"));
        assertEquals("test-message", getProperty(event.getStringProperties(), "message"));

        AgentControllerProvider.shutdown();
    }
}
