/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.event;

import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Set;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;

public class EventCsvSerializer {

    private static final char CSV_SEPARATOR = ';';
    private static final String CSV_NEWLINE = "\r\n";

    private final OutputStream outputStream;
    private Set<String> eventPropertyNames;

    private EventCsvSerializer(OutputStream outputStream, Set<String> eventPropertyNames) {
        this.outputStream = outputStream;
        this.eventPropertyNames = eventPropertyNames;
    }

    public static void eventToCsv(Event event, Set<String> eventPropertyNames, OutputStream outputStream) throws IOException {
        EventCsvSerializer serializer = new EventCsvSerializer(outputStream, eventPropertyNames);
        serializer.serializeSingleEvent(event);
        serializer.finish();
    }

    public static byte[] generateHeader(Set<String> eventPropertyNames) throws IOException {
        StringBuilder result = new StringBuilder();

        result.append(Event.FIELD_ID);
        result.append(CSV_SEPARATOR);
        result.append(Event.FIELD_TYPE);
        result.append(CSV_SEPARATOR);
        result.append(Event.FIELD_SYSTEM);
        result.append(CSV_SEPARATOR);
        result.append(Event.FIELD_AGENT_ID);
        result.append(CSV_SEPARATOR);
        result.append(Event.FIELD_TIMESTAMP);
        result.append(CSV_SEPARATOR);
        result.append(Event.FIELD_PARENT_EVENT_ID);
        result.append(CSV_SEPARATOR);
        result.append(Event.FIELD_GLOBAL_EVENT_ID);
        result.append(CSV_SEPARATOR);
        result.append(Event.FIELD_MARKER);
        result.append(CSV_SEPARATOR);
        result.append(CSV_SEPARATOR);

        for (String eachPropertyName : eventPropertyNames) {
            result.append(eachPropertyName);
            result.append(CSV_SEPARATOR);
        }
        result.append(CSV_NEWLINE);

        return result.toString().getBytes("UTF-8");
    }

    private void finish() throws IOException {
        this.outputStream.write(CSV_NEWLINE.getBytes("UTF-8"));
        this.outputStream.flush();
    }

    private void serializeSingleEvent(Event event) throws IOException {
        this.writeField(event.getId());
        this.writeField(event.getType());
        this.writeField(event.getSystem());
        this.writeField(event.getAgentId());
        this.writeField(event.getTimestamp());
        this.writeField(event.getParentEventId());
        this.writeField(event.getGlobalEventId());
        this.writeField(event.getMarker());
        this.writeField(null);

        for (String propertyName : this.eventPropertyNames) {
            if (propertyName.startsWith("boolean_property_")) {
                if (!this.writeProperty(propertyName.substring("boolean_property_".length()), event.getBooleanProperties())) {
                    this.writeField(null);
                }
            }

            if (propertyName.startsWith("date_property_")) {
                if (!this.writeProperty(propertyName.substring("date_property_".length()), event.getDateProperties())) {
                    this.writeField(null);
                }
            }

            if (propertyName.startsWith("double_property_")) {
                if (!this.writeProperty(propertyName.substring("double_property_".length()), event.getDoubleProperties())) {
                    this.writeField(null);
                }
            }

            if (propertyName.startsWith("float_property_")) {
                if (!this.writeProperty(propertyName.substring("float_property_".length()), event.getFloatProperties())) {
                    this.writeField(null);
                }
            }

            if (propertyName.startsWith("integer_property_")) {
                if (!this.writeProperty(propertyName.substring("integer_property_".length()), event.getFloatProperties())) {
                    this.writeField(null);
                }
            }

            if (propertyName.startsWith("long_property_")) {
                if (!this.writeProperty(propertyName.substring("long_property_".length()), event.getLongProperties())) {
                    this.writeField(null);
                }
            }

            if (propertyName.startsWith("string_property_")) {
                if (!this.writeProperty(propertyName.substring("string_property_".length()), event.getStringProperties())) {
                    this.writeField(null);
                }
            }
        }

        // this.writeProperties(event.getBooleanProperties());
        // this.writeProperties(event.getDateProperties());
        // this.writeProperties(event.getDoubleProperties());
        // this.writeProperties(event.getFloatProperties());
        // this.writeProperties(event.getIntegerProperties());
        // this.writeProperties(event.getLongProperties());
        // this.writeProperties(event.getStringProperties());
    }

    private void writeField(Object value) throws IOException {
        this.outputStream.write(MessageFormat.format("{0}{1}", null == value ? "" : value, CSV_SEPARATOR).getBytes("UTF-8"));
    }

    private boolean writeProperty(String propertyName, Collection<? extends EventProperty<?>> properties) throws IOException {
        for (EventProperty<?> eventProperty : properties) {
            if (eventProperty.getKey().equals(propertyName)) {
                this.writeField(eventProperty.getValue());
                return true;
            }
        }
        return false;
    }
}
