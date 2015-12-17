/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import io.logspace.agent.api.event.EventProperties;
import io.logspace.agent.api.event.EventProperty;

public abstract class AbstractEventPropertyJsonHandler<T> implements EventPropertyJsonHandler<T> {

    @Override
    public void readEventProperties(EventProperties eventProperties, String propertyName, JsonParser jsonParser) throws IOException {
        while (true) {
            JacksonUtils.prepareToken(jsonParser);
            if (jsonParser.getCurrentToken() == JsonToken.END_ARRAY) {
                break;
            }

            this.readPropertyValue(eventProperties, propertyName, jsonParser);

            JacksonUtils.consumeToken(jsonParser);
        }
    }

    @Override
    public void writeEventProperties(JsonGenerator jsonGenerator, Collection<? extends EventProperty<T>> properties)
            throws IOException {
        Map<String, List<T>> propertyValues = this.getPropertyValues(properties);

        jsonGenerator.writeFieldName(this.getFieldName());

        jsonGenerator.writeStartObject();
        for (Entry<String, List<T>> eachEntry : propertyValues.entrySet()) {
            jsonGenerator.writeArrayFieldStart(eachEntry.getKey());

            for (T eachPropertyValue : eachEntry.getValue()) {
                this.writePropertyValue(jsonGenerator, eachPropertyValue);
            }
            jsonGenerator.writeEndArray();
        }

        jsonGenerator.writeEndObject();
    }

    protected abstract void readPropertyValue(EventProperties eventProperties, String propertyName, JsonParser jsonParser)
            throws IOException;

    protected abstract void writePropertyValue(JsonGenerator jsonGenerator, T propertyValue) throws IOException;

    private Map<String, List<T>> getPropertyValues(Collection<? extends EventProperty<T>> properties) {
        Map<String, List<T>> result = new LinkedHashMap<String, List<T>>();

        for (EventProperty<T> eachProperty : properties) {
            List<T> values = result.get(eachProperty.getKey());
            if (values == null) {
                values = new ArrayList<T>();
                result.put(eachProperty.getKey(), values);
            }

            values.add(eachProperty.getValue());
        }

        return result;
    }
}
