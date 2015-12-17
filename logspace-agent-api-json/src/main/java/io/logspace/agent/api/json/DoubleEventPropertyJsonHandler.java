/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import io.logspace.agent.api.event.DoubleEventProperty;
import io.logspace.agent.api.event.EventProperties;

public class DoubleEventPropertyJsonHandler extends AbstractEventPropertyJsonHandler<Double> {

    public static final String FIELD_NAME = "double-properties";

    @Override
    public String getFieldName() {
        return FIELD_NAME;
    }

    @Override
    protected void readPropertyValue(EventProperties eventProperties, String propertyName, JsonParser jsonParser) throws IOException {
        double propertyValue = jsonParser.getDoubleValue();
        eventProperties.add(new DoubleEventProperty(propertyName, propertyValue));
    }

    @Override
    protected void writePropertyValue(JsonGenerator jsonGenerator, Double propertyValue) throws IOException {
        jsonGenerator.writeNumber(propertyValue);
    }
}
