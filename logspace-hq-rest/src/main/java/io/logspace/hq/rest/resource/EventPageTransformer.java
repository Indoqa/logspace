/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.resource;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.logspace.agent.api.json.AbstractJsonSerializer;
import io.logspace.agent.api.json.EventJsonSerializer;
import io.logspace.hq.core.api.event.EventPage;
import spark.ResponseTransformer;

public class EventPageTransformer implements ResponseTransformer {

    public static final EventPageTransformer EVENT_PAGE_TRANSFORMER = new EventPageTransformer();

    @Override
    public String render(Object model) throws Exception {
        if (!(model instanceof EventPage)) {
            throw new IllegalArgumentException("Cannot render " + model + ". Expected an object of type EventPage.");
        }

        return EventPageSerializer.toJson((EventPage) model);
    }

    public static class EventPageSerializer extends AbstractJsonSerializer {

        private EventPageSerializer(OutputStream outputStream) throws IOException {
            super(outputStream);
        }

        public static String toJson(EventPage eventPage) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            toJson(eventPage, baos);

            return baos.toString(UTF8.getJavaName());
        }

        public static void toJson(EventPage eventPage, OutputStream outputStream) throws IOException {
            EventPageSerializer serializer = new EventPageSerializer(outputStream);
            serializer.serialize(eventPage);
            serializer.finish();
        }

        private void serialize(EventPage eventPage) throws IOException {
            this.startObject();

            this.writeMandatoryLongField("totalCount", eventPage.getTotalCount());
            this.writeMandatoryStringField("nextCursorMark", eventPage.getNextCursorMark());

            this.writeField("events");
            EventJsonSerializer.toJson(eventPage.getEvents(), this.getJsonGenerator());

            this.endObject();
        }
    }
}
