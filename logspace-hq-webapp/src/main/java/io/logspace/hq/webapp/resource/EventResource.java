/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp.resource;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.EventJsonDeserializer;
import io.logspace.hq.core.api.EventService;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;

import com.indoqa.boot.AbstractJsonResourcesBase;

@Named
public class EventResource extends AbstractJsonResourcesBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private EventService eventService;

    @PostConstruct
    public void mount() {
        this.post("/events", (req, res) -> this.logEvents(req, res));
    }

    private Void logEvents(Request req, Response res) throws IOException {
        this.logger.info(req.body());

        Collection<? extends Event> events = EventJsonDeserializer.fromJson(req.bodyAsBytes());
        this.eventService.store(events);

        res.status(202);
        return null;
    }
}
