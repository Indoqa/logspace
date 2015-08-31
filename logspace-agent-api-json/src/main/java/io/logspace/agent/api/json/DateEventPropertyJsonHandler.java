/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import io.logspace.agent.api.event.DateEventProperty;
import io.logspace.agent.api.event.EventProperties;
import io.logspace.agent.api.event.EventProperty;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

public class DateEventPropertyJsonHandler implements EventPropertyJsonHandler<Date> {

    public static final String FIELD_NAME = "date-properties";

    @Override
    public String getFieldName() {
        return FIELD_NAME;
    }

    @Override
    public void readEventProperty(EventProperties eventProperties, JsonParser jsonParser) throws IOException {
        String propertyKey = jsonParser.getCurrentName();

        jsonParser.nextToken();
        String propertyValue = jsonParser.getText();

        eventProperties.add(new DateEventProperty(propertyKey, JacksonUtils.parseDateValue(propertyValue)));
    }

    @Override
    public void writeEventProperty(EventProperty<Date> eventProperty, JsonGenerator jsonGenerator) throws IOException {
        JacksonUtils.writeMandatoryDateField(jsonGenerator, eventProperty.getKey(), eventProperty.getValue());
    }
}
