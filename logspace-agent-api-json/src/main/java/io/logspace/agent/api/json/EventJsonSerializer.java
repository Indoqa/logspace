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
import io.logspace.agent.api.event.Optional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public final class EventJsonSerializer {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String TIMEZONE_UTC = "UTC";

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

    public static String toJson(Collection<Event> events) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = createJsonGenerator(baos);

        jsonGenerator.writeStartArray();

        for (Event eachEvent : events) {
            jsonGenerator.writeStartObject();

            writeId(jsonGenerator, eachEvent.getId());
            writeType(jsonGenerator, eachEvent.getType());
            writeTimestamp(jsonGenerator, eachEvent.getTimestamp());
            writeParentId(jsonGenerator, eachEvent.getParentEventId());
            writeGlobalId(jsonGenerator, eachEvent.getGlobalEventId());

            writeProperties(jsonGenerator, eachEvent.getProperties());

            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();

        jsonGenerator.flush();
        return baos.toString(UTF8.getJavaName());
    }

    private static JsonGenerator createJsonGenerator(ByteArrayOutputStream baos) throws IOException {
        JsonGenerator jsonGenerator = JSON_FACTORY.createGenerator(baos, UTF8);

        if (LOGGER.isDebugEnabled()) {
            jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
        }

        return jsonGenerator;
    }

    private static void writeGlobalId(JsonGenerator jsonGenerator, Optional<String> globalEventId) throws IOException {
        if (globalEventId.isPresent()) {
            jsonGenerator.writeStringField(FIELD_GLOBAL_EVENT_ID, globalEventId.get());
        }
    }

    private static void writeId(JsonGenerator jsonGenerator, String id) throws IOException {
        jsonGenerator.writeStringField(FIELD_ID, id);
    }

    private static void writeParentId(JsonGenerator jsonGenerator, Optional<String> parentEventId) throws IOException {
        if (parentEventId.isPresent()) {
            jsonGenerator.writeStringField(FIELD_PARENT_EVENT_ID, parentEventId.get());
        }
    }

    private static void writeProperties(JsonGenerator jsonGenerator, Collection<EventProperty> properties) throws IOException {
        if (properties.isEmpty()) {
            return;
        }

        jsonGenerator.writeFieldName(FIELD_PROPERTIES);
        jsonGenerator.writeStartObject();
        for (EventProperty eachProperty : properties) {
            jsonGenerator.writeStringField(eachProperty.getKey(), eachProperty.getValue());
        }
        jsonGenerator.writeEndObject();
    }

    private static void writeTimestamp(JsonGenerator jsonGenerator, Date timestamp) throws IOException {
        DateFormat df = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
        jsonGenerator.writeStringField(FIELD_TIMESTAMP, df.format(timestamp));
    }

    private static void writeType(JsonGenerator jsonGenerator, Optional<String> type) throws IOException {
        if (type.isPresent()) {
            jsonGenerator.writeStringField(FIELD_TYPE, type.get());
        }
    }
}
