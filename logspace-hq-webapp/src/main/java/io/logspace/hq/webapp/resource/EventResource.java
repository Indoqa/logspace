/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp.resource;

import io.logspace.agent.api.json.EventJsonDeserializer;
import io.logspace.hq.core.api.EventService;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

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

    private Void logEvents(Request req, Response res) throws IOException {
        String space = this.getSpace(req);

        this.eventService.store(EventJsonDeserializer.fromJson(req.bodyAsBytes()), space);

        res.status(202);
        return null;
    }
}
