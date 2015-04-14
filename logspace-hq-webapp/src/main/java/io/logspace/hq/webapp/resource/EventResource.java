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
import io.logspace.hq.webapp.Spaces;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
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

    @Inject
    private Spaces spaces;

    @PostConstruct
    public void mount() {
        this.post("/events", (req, res) -> this.logEvents(req, res));
    }

    private Void logEvents(Request req, Response res) throws IOException {
        this.logger.info(req.body());

        String spaceToken = req.headers("logspace.space-token");
        if (StringUtils.isBlank(spaceToken)) {
            this.logger.info("Rejecting events because the space-token is missing.");
            res.status(403);
            res.body("Missing space-token.");
            return null;
        }

        String space = this.spaces.getSpaceForAuthenticationToken(spaceToken);
        if (space == null) {
            this.logger.info("Rejecting events because the space-token '{}' is not recognized.", spaceToken);
            res.status(403);
            res.body("Unrecognized space-token '" + spaceToken + "'.");
            return null;
        }

        Collection<? extends Event> events = EventJsonDeserializer.fromJson(req.bodyAsBytes());
        this.eventService.store(events, space);

        res.status(202);
        return null;
    }
}
