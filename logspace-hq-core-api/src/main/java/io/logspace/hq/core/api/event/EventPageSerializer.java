/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.event;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.logspace.agent.api.json.EventJsonSerializer;
import io.logspace.agent.api.json.JacksonUtils;

public class EventPageSerializer extends JsonSerializer<EventPage> {

    @Override
    public void serialize(EventPage eventPage, JsonGenerator generator, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        generator.writeStartObject();

        JacksonUtils.writeMandatoryLongField(generator, "totalCount", eventPage.getTotalCount());
        JacksonUtils.writeMandatoryStringField(generator, "nextCursorMark", eventPage.getNextCursorMark());

        generator.writeFieldName("events");
        EventJsonSerializer.toJson(eventPage.getEvents(), generator);

        generator.writeEndObject();
    }
}
