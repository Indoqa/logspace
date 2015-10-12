/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.resource;

import static io.logspace.agent.api.HttpStatusCode.Accepted;
import static io.logspace.hq.rest.transformer.EventPageTransformer.EVENT_PAGE_TRANSFORMER;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.EventJsonDeserializer;
import io.logspace.hq.core.api.event.EventFilter;
import io.logspace.hq.core.api.event.EventPage;
import io.logspace.hq.core.api.event.EventService;
import spark.Request;
import spark.Response;

@Named
public class EventResource extends AbstractSpaceResource {

    private static final String PARAMETER_FILTER = "filter";
    private static final String PARAMETER_CURSOR = "cursor";

    private static final String PARAMETER_COUNT = "count";
    private static final int DEFAULT_COUNT = 10;
    private static final int MIN_COUNT = 0;
    private static final int MAX_RETRIEVAL_COUNT = 1000;

    @Inject
    private EventService eventService;

    @PostConstruct
    public void mount() {
        this.get("/events", (req, res) -> this.getEvents(req), EVENT_PAGE_TRANSFORMER);
        this.put("/events", (req, res) -> this.putEvents(req, res));
        this.post("/events", (req, res) -> this.postEvents(req), EVENT_PAGE_TRANSFORMER);
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
}
