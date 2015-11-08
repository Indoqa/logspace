/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;
import static io.logspace.agent.api.event.Event.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonGenerator;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;

public final class EventJsonSerializer extends AbstractJsonSerializer {

    private EventJsonSerializer(JsonGenerator jsonGenerator) {
        super(jsonGenerator);
    }

    private EventJsonSerializer(OutputStream outputStream) throws IOException {
        super(outputStream);
    }

    public static void eventToJson(Event event, OutputStream outputStream) throws IOException {
        EventJsonSerializer serializer = new EventJsonSerializer(outputStream);
        serializer.serializeSingleEvent(event);
        serializer.finish();
    }

    public static String toJson(Collection<? extends Event> events) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        toJson(events, baos);

        return baos.toString(UTF8.getJavaName());
    }

    public static void toJson(Collection<? extends Event> events, JsonGenerator jsonGenerator) throws IOException {
        EventJsonSerializer serializer = new EventJsonSerializer(jsonGenerator);
        serializer.serialize(events);
    }

    public static void toJson(Collection<? extends Event> events, OutputStream outputStream) throws IOException {
        EventJsonSerializer serializer = new EventJsonSerializer(outputStream);
        serializer.serialize(events);
        serializer.finish();
    }

    private void serialize(Collection<? extends Event> events) throws IOException {
        this.startArray();

        for (Event eachEvent : events) {
            this.serializeSingleEvent(eachEvent);
        }

        this.endArray();
    }

    private void serializeSingleEvent(Event event) throws IOException {
        this.startObject();

        this.writeEvent(event);

        this.endObject();
    }

    private void writeEvent(Event event) throws IOException {
        this.writeMandatoryStringField(FIELD_ID, event.getId());
        this.writeOptionalField(FIELD_TYPE, event.getType());
        this.writeMandatoryStringField(FIELD_SYSTEM, event.getSystem());
        this.writeMandatoryStringField(FIELD_AGENT_ID, event.getAgentId());
        this.writeMandatoryDateField(FIELD_TIMESTAMP, event.getTimestamp());
        this.writeOptionalField(FIELD_PARENT_EVENT_ID, event.getParentEventId());
        this.writeOptionalField(FIELD_GLOBAL_EVENT_ID, event.getGlobalEventId());

        this.writeProperties(EventPropertyJsonHandlers.getBooleanHandler(), event.getBooleanProperties());
        this.writeProperties(EventPropertyJsonHandlers.getDateHandler(), event.getDateProperties());
        this.writeProperties(EventPropertyJsonHandlers.getDoubleHandler(), event.getDoubleProperties());
        this.writeProperties(EventPropertyJsonHandlers.getFloatHandler(), event.getFloatProperties());
        this.writeProperties(EventPropertyJsonHandlers.getIntegerHandler(), event.getIntegerProperties());
        this.writeProperties(EventPropertyJsonHandlers.getLongHandler(), event.getLongProperties());
        this.writeProperties(EventPropertyJsonHandlers.getStringHandler(), event.getStringProperties());
    }

    private <T> void writeProperties(EventPropertyJsonHandler<T> handler, Collection<? extends EventProperty<T>> properties)
            throws IOException {
        if (properties == null || properties.isEmpty()) {
            return;
        }

        this.writeFieldName(handler.getFieldName());

        this.startObject();
        for (EventProperty<T> eachProperty : properties) {
            handler.writeEventProperty(eachProperty, this.getJsonGenerator());
        }
        this.endObject();
    }
}
