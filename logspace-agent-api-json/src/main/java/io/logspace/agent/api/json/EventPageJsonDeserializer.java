/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;

public class EventPageJsonDeserializer extends AbstractJsonDeserializer {

    private EventPageJsonDeserializer(byte[] data) throws IOException {
        super();

        this.setData(data);
    }

    private EventPageJsonDeserializer(InputStream inputStream) throws IOException {
        super();

        this.setInputStream(inputStream);
    }

    public static EventPage fromJson(byte[] data) throws IOException {
        EventPageJsonDeserializer deserializer = new EventPageJsonDeserializer(data);
        return deserializer.deserialize();
    }

    public static EventPage fromJson(InputStream inputStream) throws IOException {
        EventPageJsonDeserializer deserializer = new EventPageJsonDeserializer(inputStream);
        return deserializer.deserialize();
    }

    public EventPage deserialize() throws IOException, JsonProcessingException {
        this.prepareToken();
        this.validateTokenType(JsonToken.START_OBJECT);
        this.consumeToken();

        EventPage eventPage = new EventPage();

        eventPage.setTotalCount(this.readMandatoryLongField("totalCount"));
        eventPage.setNextCursorMark(this.readMandatoryField("nextCursorMark"));

        this.prepareToken();
        this.validateTokenType(FIELD_NAME);
        this.validateFieldName("events");

        eventPage.setEvents(EventJsonDeserializer.fromJson(this.getJsonParser()));

        this.prepareToken();
        this.validateTokenType(JsonToken.END_OBJECT);
        this.consumeToken();
        this.validateEnd();

        return eventPage;
    }
}
