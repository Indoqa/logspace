/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;

import org.apache.commons.lang.StringUtils;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.EventJsonSerializer;
import io.logspace.hq.core.api.event.EventService;
import io.logspace.hq.core.api.event.EventService.EventStreamer;
import io.logspace.hq.rest.api.event.EventFilter;
import spark.Request;
import spark.Response;

@Named
public class EventStreamResource extends AbstractSpaceResource {

    private static final String PARAMETER_COUNT = "count";
    private static final int DEFAULT_COUNT = 10;
    private static final int MIN_COUNT = 0;
    private static final int MAX_STREAM_COUNT = Integer.MAX_VALUE;

    private static final String PARAMETER_OFFSET = "offset";
    private static final int DEFAULT_STREAM_OFFSET = 0;
    private static final int MIN_OFFSET = 0;
    private static final int MAX_OFFSET = MAX_STREAM_COUNT;

    @Inject
    private EventService eventService;

    @PostConstruct
    public void mount() {
        this.post("/event-stream", (req, res) -> this.postEventStream(req, res));
    }

    private Object postEventStream(Request req, Response res) throws IOException {
        EventFilter eventFilter = this.readFilter(req.body());
        int offset = getQueryParam(req, PARAMETER_OFFSET, DEFAULT_STREAM_OFFSET, MIN_OFFSET, MAX_OFFSET);
        int count = getQueryParam(req, PARAMETER_COUNT, DEFAULT_COUNT, MIN_COUNT, MAX_STREAM_COUNT);

        res.raw().setContentType("application/json;charset=UTF-8");

        ServletOutputStream outputStream = res.raw().getOutputStream();
        outputStream.print('[');
        this.eventService.stream(eventFilter, count, offset, new JsonEventStreamer(outputStream));
        outputStream.print(']');
        outputStream.close();

        return "";
    }

    private EventFilter readFilter(String filterParam) {
        if (StringUtils.isNotBlank(filterParam)) {
            return this.getTransformer().toObject(filterParam, EventFilter.class);
        }

        return new EventFilter();
    }

    private static final class JsonEventStreamer implements EventStreamer {

        private final OutputStream outputStream;

        private boolean first;

        public JsonEventStreamer(OutputStream outputStream) {
            super();
            this.outputStream = outputStream;
            this.first = true;
        }

        @Override
        public void streamEvent(Event event) throws IOException {
            if (this.first) {
                this.first = false;
            } else {
                this.outputStream.write(',');
                this.outputStream.write(' ');
            }

            EventJsonSerializer.eventToJson(event, this.outputStream);
        }
    }
}
