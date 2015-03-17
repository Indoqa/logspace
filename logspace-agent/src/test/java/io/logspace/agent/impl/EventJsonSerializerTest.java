/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.impl;

import io.logspace.agent.api.AgentController;
import io.logspace.agent.api.event.DefaultEventBuilder;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.agent.api.event.ImmutableEvent;
import io.logspace.agent.api.event.Optional;
import io.logspace.agent.api.json.EventJsonSerializer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;

public class EventJsonSerializerTest {

    public void agentControllerQuartz() {
        HqAgentController.install("http://localhost:8080/test");
        AgentController controller = AgentControllerProvider.getAgentController();

        try {
            Thread.sleep(110000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            controller.shutdown();
        }
    }

    @Test
    public void test() throws IOException {
        Collection<Event> events = new HashSet<Event>();
        events.add(new ImmutableEvent(Optional.of("typeA"), Optional.of("1"), Optional.of("1"), new EventProperty("foo", "bar")));
        events.add(new ImmutableEvent(Optional.of("typeB"), Optional.of("2"), Optional.of("2"), new EventProperty("foo", "bar"),
                new EventProperty("foo2", "bar2")));
        events.add(new DefaultEventBuilder().setGlobalEventId("345").setParentEventId("678").toEvent());

        String json = EventJsonSerializer.toJson(events);
        System.out.println(json);
    }
}
