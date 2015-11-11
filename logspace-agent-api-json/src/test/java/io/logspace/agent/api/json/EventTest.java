/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static io.logspace.agent.api.json.RandomHelper.*;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import org.junit.Test;

import io.logspace.agent.api.event.*;

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

    @Test
    public void testSingleEvent() throws IOException {
        for (int i = 0; i < 100; i++) {
            Event expected = this.createRandomEvent();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            EventJsonSerializer.eventToJson(expected, baos);
            Event actual = EventJsonDeserializer.eventFromJson(baos.toByteArray());

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
        assertEquals(expected.getSystem(), actual.getSystem());
        assertEquals(expected.getAgentId(), actual.getAgentId());
        assertEquals(expected.getGlobalEventId(), actual.getGlobalEventId());
        assertEquals(expected.getParentEventId(), actual.getParentEventId());
        assertEquals(expected.getTimestamp().getTime(), actual.getTimestamp().getTime());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getMarker(), actual.getMarker());

        assertEquals(expected.getBooleanProperties(), actual.getBooleanProperties());
        assertEquals(expected.getDateProperties(), actual.getDateProperties());
        assertEquals(expected.getDoubleProperties(), actual.getDoubleProperties());
        assertEquals(expected.getFloatProperties(), actual.getFloatProperties());
        assertEquals(expected.getIntegerProperties(), actual.getIntegerProperties());
        assertEquals(expected.getLongProperties(), actual.getLongProperties());
        assertEquals(expected.getStringProperties(), actual.getStringProperties());
    }

    private Event createRandomEvent() {
        TestEvent result = new TestEvent();

        result.setGlobalEventId(getRandomOptionalString());
        result.setId(getRandomString());
        result.setSystem(getRandomString());
        result.setAgentId(getRandomString());
        result.setParentEventId(getRandomOptionalString());
        result.setProperties(this.createRandomProperties());
        result.setTimestamp(new Date());
        result.setType(getRandomOptionalString());
        result.setMarker(getRandomOptionalString());

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

    private EventProperties createRandomProperties() {
        EventProperties result = new EventProperties();

        int count = RandomHelper.getRandomCount(5);
        for (int i = 0; i < count; i++) {
            switch (RandomHelper.getRandomCount(7)) {
                case 0:
                    result.add(new BooleanEventProperty(getRandomString(), getRandomBoolean()));
                    break;

                case 1:
                    result.add(new DateEventProperty(getRandomString(), getRandomDate()));
                    break;

                case 2:
                    result.add(new DoubleEventProperty(getRandomString(), getRandomDouble()));
                    break;

                case 3:
                    result.add(new FloatEventProperty(getRandomString(), getRandomFloat()));
                    break;

                case 4:
                    result.add(new IntegerEventProperty(getRandomString(), getRandomInt()));
                    break;

                case 5:
                    result.add(new LongEventProperty(getRandomString(), getRandomLong()));
                    break;

                case 6:
                    result.add(new StringEventProperty(getRandomString(), getRandomString()));
                    break;
            }
        }

        return result;
    }

    private class TestEvent implements Event {

        private String id;
        private String system;
        private String agentId;
        private String globalEventId;
        private String parentEventId;
        private String type;
        private Date timestamp;
        private String marker;

        private EventProperties properties;

        @Override
        public String getAgentId() {
            return this.agentId;
        }

        @Override
        public Collection<BooleanEventProperty> getBooleanProperties() {
            return this.properties.getBooleanProperties();
        }

        @Override
        public Collection<DateEventProperty> getDateProperties() {
            return this.properties.getDateProperties();
        }

        @Override
        public Collection<DoubleEventProperty> getDoubleProperties() {
            return this.properties.getDoubleProperties();
        }

        @Override
        public Collection<FloatEventProperty> getFloatProperties() {
            return this.properties.getFloatProperties();
        }

        @Override
        public String getGlobalEventId() {
            return this.globalEventId;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public Collection<IntegerEventProperty> getIntegerProperties() {
            return this.properties.getIntegerProperties();
        }

        @Override
        public Collection<LongEventProperty> getLongProperties() {
            return this.properties.getLongProperties();
        }

        @Override
        public String getMarker() {
            return this.marker;
        }

        @Override
        public String getParentEventId() {
            return this.parentEventId;
        }

        @Override
        public Collection<StringEventProperty> getStringProperties() {
            return this.properties.getStringProperties();
        }

        @Override
        public String getSystem() {
            return this.system;
        }

        @Override
        public Date getTimestamp() {
            return this.timestamp;
        }

        @Override
        public String getType() {
            return this.type;
        }

        @Override
        public boolean hasProperties() {
            return this.properties != null && !this.properties.isEmpty();
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public void setGlobalEventId(String globalEventId) {
            this.globalEventId = globalEventId;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setMarker(String marker) {
            this.marker = marker;
        }

        public void setParentEventId(String parentEventId) {
            this.parentEventId = parentEventId;
        }

        public void setProperties(EventProperties properties) {
            this.properties = properties;
        }

        public void setSystem(String system) {
            this.system = system;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
