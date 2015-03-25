/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public final class EventJsonSerializer extends AbstractJsonSerializer {

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
        this.writeMandatoryField(Event.FIELD_ID, event.getId());
        this.writeOptionalField(Event.FIELD_TYPE, event.getType());
        this.writeMandatoryDateField(Event.FIELD_TIMESTAMP, event.getTimestamp());
        this.writeOptionalField(Event.FIELD_PARENT_EVENT_ID, event.getParentEventId());
        this.writeOptionalField(Event.FIELD_GLOBAL_EVENT_ID, event.getGlobalEventId());
        this.writeProperties(event.getProperties());
    }

    private void writeProperties(Collection<EventProperty> properties) throws IOException {
        if (properties == null || properties.isEmpty()) {
            return;
        }

        this.writeField(Event.FIELD_PROPERTIES);

        this.startObject();
        for (EventProperty eachProperty : properties) {
            this.writeMandatoryField(eachProperty.getKey(), eachProperty.getValue());
        }
        this.endObject();
    }
}
