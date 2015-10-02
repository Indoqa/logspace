/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.resource;

import static io.logspace.agent.api.HttpStatusCode.Accepted;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.EventJsonDeserializer;
import io.logspace.hq.core.api.event.EventService;
import spark.Request;
import spark.Response;

@Named
public class EventResource extends AbstractSpaceResource {

    @Inject
    private EventService eventService;

    @PostConstruct
    public void mount() {
        this.post("/events", (req, res) -> this.logEvents(req, res));
    }

    private String logEvents(Request req, Response res) throws IOException {
        String space = this.getSpace(req);
        Collection<? extends Event> events = EventJsonDeserializer.fromJson(req.bodyAsBytes());

        this.logger.info("Storing {} event(s) for space '{}'", events.size(), space);
        this.eventService.store(events, space);

        res.status(Accepted.getCode());
        return null;
    }
}
