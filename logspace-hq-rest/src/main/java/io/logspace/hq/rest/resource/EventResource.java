/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.resource;

import static io.logspace.agent.api.HttpStatusCode.Accepted;
import static io.logspace.hq.rest.resource.EventPageTransformer.EVENT_PAGE_TRANSFORMER;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;

import org.apache.commons.lang.StringUtils;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.EventJsonDeserializer;
import io.logspace.agent.api.json.EventJsonSerializer;
import io.logspace.hq.core.api.event.EventFilter;
import io.logspace.hq.core.api.event.EventPage;
import io.logspace.hq.core.api.event.EventService;
import io.logspace.hq.core.api.event.EventService.EventStreamer;
import spark.Request;
import spark.Response;

@Named
public class EventResource extends AbstractSpaceResource {

    private static final String PARAMETER_FILTER = "filter";
    private static final String PARAMETER_CURSOR = "cursor";

    private static final String PARAMETER_COUNT = "count";
    private static final int DEFAULT_COUNT = 10;
    private static final int MIN_COUNT = 0;
    private static final int MAX_STREAM_COUNT = Integer.MAX_VALUE;
    private static final int MAX_RETRIEVAL_COUNT = 1000;

    private static final String PARAMETER_OFFSET = "offset";
    private static final int DEFAULT_STREAM_OFFSET = 0;
    private static final int MIN_OFFSET = 0;
    private static final int MAX_OFFSET = MAX_STREAM_COUNT;

    @Inject
    private EventService eventService;

    @PostConstruct
    public void mount() {
        this.put("/events", (req, res) -> this.putEvents(req, res));

        this.get("/events", (req, res) -> this.getEvents(req), EVENT_PAGE_TRANSFORMER);
        this.post("/events", (req, res) -> this.postEvents(req), EVENT_PAGE_TRANSFORMER);

        this.post("/event-stream", (req, res) -> this.postEventStream(req, res));
    }

    private EventPage getEvents(Request req) {
        EventFilter eventFilter = this.readFilter(req.params(PARAMETER_FILTER));
        int count = getQueryParam(req, PARAMETER_COUNT, DEFAULT_COUNT, MIN_COUNT, MAX_RETRIEVAL_COUNT);
        String cursor = getQueryParam(req, PARAMETER_CURSOR, "*");
        return this.retrieveEvents(eventFilter, count, cursor);
    }

    private EventPage postEvents(Request req) {
        EventFilter eventFilter = this.readFilter(req.body());
        int count = getQueryParam(req, PARAMETER_COUNT, DEFAULT_COUNT, MIN_COUNT, MAX_RETRIEVAL_COUNT);
        String cursor = getQueryParam(req, PARAMETER_CURSOR, "*");
        return this.retrieveEvents(eventFilter, count, cursor);
    }

    private Object postEventStream(Request req, Response res) throws IOException {
        EventFilter eventFilter = this.readFilter(req.body());
        int offset = getQueryParam(req, PARAMETER_OFFSET, DEFAULT_STREAM_OFFSET, MIN_OFFSET, MAX_OFFSET);
        int count = getQueryParam(req, PARAMETER_COUNT, DEFAULT_COUNT, MIN_COUNT, MAX_STREAM_COUNT);

        res.raw().setContentType("application/json");

        ServletOutputStream outputStream = res.raw().getOutputStream();
        outputStream.print('[');
        this.eventService.stream(eventFilter, count, offset, new JsonEventStreamer(outputStream));
        outputStream.print(']');
        outputStream.close();

        return "";
    }

    private Object putEvents(Request req, Response res) throws IOException {
        String space = this.getSpace(req);
        Collection<? extends Event> events = EventJsonDeserializer.fromJson(req.bodyAsBytes());

        this.eventService.store(events, space);

        res.status(Accepted.getCode());
        return "";
    }

    private EventFilter readFilter(String filterParam) {
        if (StringUtils.isNotBlank(filterParam)) {
            return this.getTransformer().toObject(filterParam, EventFilter.class);
        }

        return new EventFilter();
    }

    private EventPage retrieveEvents(EventFilter eventFilter, int count, String cursor) {
        return this.eventService.retrieve(eventFilter, count, cursor);
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
