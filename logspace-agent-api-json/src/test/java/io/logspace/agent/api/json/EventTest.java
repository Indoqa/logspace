/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static io.logspace.agent.api.json.RandomHelper.getRandomOptional;
import static io.logspace.agent.api.json.RandomHelper.getRandomString;
import static org.junit.Assert.assertEquals;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.agent.api.event.Optional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class EventTest {

    @Test
    public void test() throws IOException {
        for (int i = 0; i < 100; i++) {
            Collection<? extends Event> expected = this.createRandomEvents();

            String json = EventJsonSerializer.toJson(expected);
            Collection<? extends Event> actual = EventJsonDeserializer.fromJson(json.getBytes("UTF-8"));

            this.compare(expected, actual);
        }
    }

    private void compare(Collection<? extends Event> expected, Collection<? extends Event> actual) {
        assertEquals(expected.size(), actual.size());

        Iterator<? extends Event> expectedIterator = expected.iterator();
        Iterator<? extends Event> actualIterator = actual.iterator();
        while (expectedIterator.hasNext()) {
            this.compare(expectedIterator.next(), actualIterator.next());
        }
    }

    private void compare(Event expected, Event actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getGlobalEventId(), actual.getGlobalEventId());
        assertEquals(expected.getParentEventId(), actual.getParentEventId());
        assertEquals(expected.getTimestamp().getTime() / 1000, actual.getTimestamp().getTime() / 1000);
        assertEquals(expected.getType(), actual.getType());

        assertEquals(expected.getProperties() == null, actual.getProperties() == null);
        if (expected.getProperties() == null) {
            return;
        }

        assertEquals(expected.getProperties().size(), actual.getProperties().size());
        Iterator<EventProperty> expectedIterator = expected.getProperties().iterator();
        Iterator<EventProperty> actualIterator = actual.getProperties().iterator();
        while (expectedIterator.hasNext()) {
            this.compare(expectedIterator.next(), actualIterator.next());
        }
    }

    private void compare(EventProperty expected, EventProperty actual) {
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.getValue(), actual.getValue());
    }

    private Event createRandomEvent() {
        TestEvent result = new TestEvent();

        result.setGlobalEventId(getRandomOptional());
        result.setId(getRandomString());
        result.setParentEventId(getRandomOptional());
        result.setProperties(this.createRandomProperties());
        result.setTimestamp(new Date());
        result.setType(getRandomOptional());

        return result;
    }

    private Collection<? extends Event> createRandomEvents() {
        List<Event> result = new ArrayList<Event>();

        int count = RandomHelper.getRandomCount(5);
        for (int i = 0; i < count; i++) {
            result.add(this.createRandomEvent());
        }

        return result;
    }

    private Collection<EventProperty> createRandomProperties() {
        Collection<EventProperty> result = new ArrayList<EventProperty>();

        int count = RandomHelper.getRandomCount(5);
        for (int i = 0; i < count; i++) {
            result.add(this.createRandomProperty());
        }

        return result;
    }

    private EventProperty createRandomProperty() {
        return new EventProperty(getRandomString(), getRandomString());
    }

    private class TestEvent implements Event {

        private String id;
        private Optional<String> globalEventId;
        private Optional<String> parentEventId;
        private Collection<EventProperty> properties;
        private Optional<String> type;
        private Date timestamp;

        @Override
        public Optional<String> getGlobalEventId() {
            return this.globalEventId;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public Optional<String> getParentEventId() {
            return this.parentEventId;
        }

        @Override
        public Collection<EventProperty> getProperties() {
            return this.properties;
        }

        @Override
        public Date getTimestamp() {
            return this.timestamp;
        }

        @Override
        public Optional<String> getType() {
            return this.type;
        }

        @Override
        public boolean hasProperties() {
            return this.properties != null && !this.properties.isEmpty();
        }

        public void setGlobalEventId(Optional<String> globalEventId) {
            this.globalEventId = globalEventId;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setParentEventId(Optional<String> parentEventId) {
            this.parentEventId = parentEventId;
        }

        public void setProperties(Collection<EventProperty> properties) {
            this.properties = properties;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public void setType(Optional<String> type) {
            this.type = type;
        }
    }
}