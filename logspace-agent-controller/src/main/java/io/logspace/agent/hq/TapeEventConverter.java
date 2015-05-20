/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.hq;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.EventJsonDeserializer;
import io.logspace.agent.api.json.EventJsonSerializer;

import java.io.IOException;
import java.io.OutputStream;

import com.squareup.tape.FileObjectQueue.Converter;

public class TapeEventConverter implements Converter<Event> {

    @Override
    public Event from(byte[] bytes) throws IOException {
        return EventJsonDeserializer.eventFromJson(bytes);
    }

    @Override
    public void toStream(Event event, OutputStream outputStream) throws IOException {
        EventJsonSerializer.eventToJson(event, outputStream);
    }
}