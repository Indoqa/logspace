/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.logspace.agent.api.event.DefaultEventBuilder;

public class EventPageTest {

    @Test
    public void test() throws Exception {
        EventPage eventPage = new EventPage();
        eventPage.setTotalCount(10);
        eventPage.setNextCursorMark("abcdef");
        eventPage.addEvent(new DefaultEventBuilder("agent-id", "system").toEvent());
        eventPage.addEvent(new DefaultEventBuilder("agent-id", "system").toEvent());

        String json = EventPageJsonSerializer.toJson(eventPage);

        EventPage loadedEventPage = EventPageJsonDeserializer.fromJson(json.getBytes("UTF-8"));
        assertEquals(eventPage.getTotalCount(), loadedEventPage.getTotalCount());
        assertEquals(eventPage.getNextCursorMark(), loadedEventPage.getNextCursorMark());
        assertEquals(eventPage.getEvents().size(), loadedEventPage.getEvents().size());
    }
}
