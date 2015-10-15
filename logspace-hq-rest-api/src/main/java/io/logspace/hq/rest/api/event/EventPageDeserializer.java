/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.event;

import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import static io.logspace.agent.api.json.JacksonUtils.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import io.logspace.agent.api.json.EventJsonDeserializer;

public class EventPageDeserializer extends JsonDeserializer<EventPage> {

    @Override
    public EventPage deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        prepareToken(parser);
        validateTokenType(parser.getCurrentToken(), JsonToken.START_OBJECT);
        consumeToken(parser);

        EventPage eventPage = new EventPage();

        eventPage.setTotalCount(readMandatoryLongField(parser, "totalCount"));
        eventPage.setNextCursorMark(readMandatoryField(parser, "nextCursorMark"));

        prepareToken(parser);
        validateTokenType(parser.getCurrentToken(), FIELD_NAME);
        validateFieldName(parser.getCurrentName(), "events");
        parser.nextValue();

        eventPage.setEvents(EventJsonDeserializer.fromJson(parser));

        prepareToken(parser);
        validateTokenType(parser.getCurrentToken(), JsonToken.END_OBJECT);
        consumeToken(parser);

        return eventPage;
    }
}
