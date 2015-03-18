/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;
import static io.logspace.agent.api.json.JacksonUtils.writeMandatoryDateField;
import static io.logspace.agent.api.json.JacksonUtils.writeMandatoryField;
import static io.logspace.agent.api.json.JacksonUtils.writeOptionalField;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public final class EventJsonSerializer {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private static final String FIELD_ID = "id";
    private static final String FIELD_TIMESTAMP = "timestamp";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_PARENT_EVENT_ID = "pid";
    private static final String FIELD_GLOBAL_EVENT_ID = "gid";
    private static final String FIELD_PROPERTIES = "properties";

    private static final Logger LOGGER = LoggerFactory.getLogger(EventJsonSerializer.class);

    private EventJsonSerializer() {
        // hide utility class constructor
    }

    public static String toJson(Collection<? extends Event> events) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        toJson(events, baos);

        return baos.toString(UTF8.getJavaName());
    }

    public static void toJson(Collection<? extends Event> events, OutputStream outputStream) throws IOException {
        JsonGenerator jsonGenerator = createJsonGenerator(outputStream);

        jsonGenerator.writeStartArray();

        for (Event eachEvent : events) {
            jsonGenerator.writeStartObject();

            writeEvent(jsonGenerator, eachEvent);

            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();

        jsonGenerator.flush();
    }

    private static JsonGenerator createJsonGenerator(OutputStream baos) throws IOException {
        JsonGenerator jsonGenerator = JSON_FACTORY.createGenerator(baos, UTF8);

        if (LOGGER.isDebugEnabled()) {
            jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
        }

        return jsonGenerator;
    }

    private static void writeEvent(JsonGenerator jsonGenerator, Event event) throws IOException {
        writeMandatoryField(jsonGenerator, FIELD_ID, event.getId());
        writeOptionalField(jsonGenerator, FIELD_TYPE, event.getType());
        writeMandatoryDateField(jsonGenerator, FIELD_TIMESTAMP, event.getTimestamp());
        writeOptionalField(jsonGenerator, FIELD_PARENT_EVENT_ID, event.getParentEventId());
        writeOptionalField(jsonGenerator, FIELD_GLOBAL_EVENT_ID, event.getGlobalEventId());
        writeProperties(jsonGenerator, event.getProperties());
    }

    private static void writeProperties(JsonGenerator jsonGenerator, Collection<EventProperty> properties) throws IOException {
        if (properties == null || properties.isEmpty()) {
            return;
        }

        jsonGenerator.writeFieldName(FIELD_PROPERTIES);

        jsonGenerator.writeStartObject();
        for (EventProperty eachProperty : properties) {
            jsonGenerator.writeStringField(eachProperty.getKey(), eachProperty.getValue());
        }
        jsonGenerator.writeEndObject();
    }
}
