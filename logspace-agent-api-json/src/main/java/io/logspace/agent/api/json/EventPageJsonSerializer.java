/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EventPageJsonSerializer extends AbstractJsonSerializer {

    private EventPageJsonSerializer(OutputStream outputStream) throws IOException {
        super(outputStream);
    }

    public static String toJson(EventPage eventPage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        toJson(eventPage, baos);

        return baos.toString(UTF8.getJavaName());
    }

    public static void toJson(EventPage eventPage, OutputStream outputStream) throws IOException {
        EventPageJsonSerializer serializer = new EventPageJsonSerializer(outputStream);
        serializer.serialize(eventPage);
        serializer.finish();
    }

    public void serialize(EventPage eventPage) throws IOException {
        this.startObject();

        this.writeMandatoryLongField("totalCount", eventPage.getTotalCount());
        this.writeMandatoryStringField("nextCursorMark", eventPage.getNextCursorMark());

        this.writeFieldName("events");
        EventJsonSerializer.toJson(eventPage.getEvents(), this.getJsonGenerator());

        this.endObject();
    }
}
