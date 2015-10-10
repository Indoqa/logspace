/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.resource;

import static io.logspace.agent.api.HttpStatusCode.Accepted;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.EventJsonDeserializer;
import io.logspace.hq.core.api.event.EventFilter;
import io.logspace.hq.core.api.event.EventPage;
import io.logspace.hq.core.api.event.EventService;
import io.logspace.hq.core.api.model.ParameterValueException;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import spark.Request;
import spark.Response;

@Named
public class EventResource extends AbstractSpaceResource {

    private static final String PARAMETER_FILTER = "filter";
    private static final String PARAMETER_CURSOR = "cursor";
    private static final String PARAMETER_COUNT = "count";

    private static final int MIN_COUNT = 1;
    private static final int MAX_COUNT = 1000;

    @Inject
    private EventService eventService;

    private static int getParam(Request request, String name, int defaultValue, int minValue, int maxValue) {
        String value = StringUtils.trim(request.params(name));
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }

        int result;
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw ParameterValueException.unparsableValue(name, value);
        }

        if (result < minValue) {
            throw ParameterValueException.valueTooSmall(name, result, minValue);
        }

        if (result > maxValue) {
            throw ParameterValueException.valueTooLarge(name, result, maxValue);
        }

        return result;
    }

    private static String getParam(Request req, String name, String defaultValue) {
        String value = StringUtils.trim(req.params(name));
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }

        return value;
    }

    @PostConstruct
    public void mount() {
        this.put("/events", (req, res) -> this.putEvents(req, res));

        this.get("/events", (req, res) -> this.getEvents(req, res));
        this.post("/events", (req, res) -> this.postEvents(req, res));
    }

    private EventPage getEvents(Request req, Response res) {
        EventFilter eventFilter = this.readFilter(req.params(PARAMETER_FILTER));
        int count = getParam(req, PARAMETER_COUNT, 10, MIN_COUNT, MAX_COUNT);
        String cursor = getParam(req, PARAMETER_CURSOR, "*");
        return this.retrieveEvents(eventFilter, count, cursor);
    }

    private EventPage postEvents(Request req, Response res) {
        EventFilter eventFilter = this.readFilter(req.body());
        int count = getParam(req, PARAMETER_COUNT, 10, MIN_COUNT, MAX_COUNT);
        String cursor = getParam(req, PARAMETER_CURSOR, "*");
        return this.retrieveEvents(eventFilter, count, cursor);
    }

    private String putEvents(Request req, Response res) throws IOException {
        String space = this.getSpace(req);
        Collection<? extends Event> events = EventJsonDeserializer.fromJson(req.bodyAsBytes());

        this.eventService.store(events, space);

        res.status(Accepted.getCode());
        return null;
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