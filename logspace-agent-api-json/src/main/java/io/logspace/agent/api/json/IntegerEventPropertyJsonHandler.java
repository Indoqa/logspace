/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import io.logspace.agent.api.event.EventProperties;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.agent.api.event.IntegerEventProperty;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

public class IntegerEventPropertyJsonHandler implements EventPropertyJsonHandler<Integer> {

    public static final String FIELD_NAME = "integer-properties";

    @Override
    public String getFieldName() {
        return FIELD_NAME;
    }

    @Override
    public void readEventProperty(EventProperties eventProperties, JsonParser jsonParser) throws IOException {
        String propertyKey = jsonParser.getCurrentName();

        jsonParser.nextToken();
        int propertyValue = jsonParser.getIntValue();

        eventProperties.add(new IntegerEventProperty(propertyKey, propertyValue));
    }

    @Override
    public void writeEventProperty(EventProperty<Integer> eventProperty, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeNumberField(eventProperty.getKey(), eventProperty.getValue());
    }
}