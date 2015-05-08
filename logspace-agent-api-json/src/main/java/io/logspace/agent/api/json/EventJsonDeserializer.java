/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonToken.END_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static io.logspace.agent.api.event.Event.*;
import io.logspace.agent.api.event.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public final class EventJsonDeserializer extends AbstractJsonDeserializer {

    private EventJsonDeserializer(byte[] data) throws IOException {
        super(data);
    }

    private EventJsonDeserializer(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    public static Event eventFromJson(byte[] data) throws IOException {
        return new EventJsonDeserializer(data).deserializeSingleEvent();
    }

    public static Collection<? extends Event> fromJson(byte[] data) throws IOException {
        return new EventJsonDeserializer(data).deserialize();
    }

    public static Collection<? extends Event> fromJson(InputStream inputStream) throws IOException {
        return new EventJsonDeserializer(inputStream).deserialize();
    }

    private Collection<? extends Event> deserialize() throws IOException {
        Collection<Event> result = new ArrayList<Event>();

        this.prepareToken();
        this.validateToken(START_ARRAY);
        this.consumeToken();

        while (true) {
            this.prepareToken();

            if (this.hasToken(END_ARRAY)) {
                this.consumeToken();
                break;
            }

            result.add(this.deserializeSingleEvent());
        }

        this.prepareToken();
        this.validateEnd();

        return result;
    }

    private Event deserializeSingleEvent() throws IOException {
        this.prepareToken();
        this.validateToken(START_OBJECT);
        this.consumeToken();

        Event result = this.readEvent();

        this.prepareToken();
        this.validateToken(END_OBJECT);
        this.consumeToken();

        return result;
    }

    private Event readEvent() throws IOException {
        DeserializedEvent event = new DeserializedEvent();

        event.setId(this.readMandatoryField(FIELD_ID));
        event.setType(this.readOptionalField(FIELD_TYPE));
        event.setSystem(this.readMandatoryField(FIELD_SYSTEM));
        event.setAgentId(this.readMandatoryField(FIELD_AGENT_ID));
        event.setTimestamp(this.readMandatoryDateField(FIELD_TIMESTAMP));
        event.setParentEventId(this.readOptionalField(FIELD_PARENT_EVENT_ID));
        event.setGlobalEventId(this.readOptionalField(FIELD_GLOBAL_EVENT_ID));
        event.setProperties(this.readProperties());

        return event;
    }

    private EventProperties readProperties() throws IOException {
        EventProperties result = new EventProperties();

        while (true) {
            this.prepareToken();
            if (!this.hasToken(FIELD_NAME)) {
                return result;
            }

            String fieldName = this.getCurrentName();
            EventPropertyJsonHandler<?> eventPropertyJsonHandler = EventPropertyJsonHandlers.getHandler(fieldName);

            this.validateField(FIELD_BOOLEAN_PROPERTIES, FIELD_DATE_PROPERTIES, FIELD_DOUBLE_PROPERTIES, FIELD_FLOAT_PROPERTIES,
                    FIELD_INTEGER_PROPERTIES, FIELD_LONG_PROPERTIES, FIELD_STRING_PROPERTIES);

            this.prepareToken();
            this.validateToken(START_OBJECT);
            this.consumeToken();

            while (true) {
                this.prepareToken();
                this.validateToken(FIELD_NAME, END_OBJECT);

                if (this.hasToken(END_OBJECT)) {
                    this.consumeToken();
                    break;
                }

                this.validateToken(FIELD_NAME);

                eventPropertyJsonHandler.readEventProperty(result, this.getJsonParser());
                this.consumeToken();
            }
        }
    }

    public static class DeserializedEvent implements Event {

        private String id;
        private String system;
        private String agentId;
        private Date timestamp;
        private Optional<String> parentEventId;
        private Optional<String> globalEventId;
        private Optional<String> type;
        private EventProperties properties = new EventProperties();

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
        public Optional<String> getGlobalEventId() {
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
        public Optional<String> getParentEventId() {
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
        public Optional<String> getType() {
            return this.type;
        }

        @Override
        public boolean hasProperties() {
            return this.properties != null && !this.properties.isEmpty();
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
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

        public void setProperties(EventProperties properties) {
            this.properties = properties;
        }

        public void setSystem(String system) {
            this.system = system;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public void setType(Optional<String> type) {
            this.type = type;
        }
    }
}