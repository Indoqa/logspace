/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp.resource;

import io.logspace.hq.core.api.EventService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;

import com.indoqa.spark.AbstractJsonResourcesBase;

@Named
public class EventResource extends AbstractJsonResourcesBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private EventService eventService;

    @PostConstruct
    public void mount() {
        this.post("/events", (req, res) -> this.logEvents(req, res));
    }

    private String logEvents(Request req, Response res) {
        this.logger.info(req.body());
        this.eventService.store(null);

        res.status(202);
        return "";
    }
}
