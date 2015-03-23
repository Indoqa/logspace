/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp.resource;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;

import com.indoqa.boot.AbstractJsonResourcesBase;

@Named
public class CapabilitiesResource extends AbstractJsonResourcesBase {

    private static final String PARAMETER_CONTROLLER_ID = "controller-id";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void mount() {
        this.put("/capabilities/:" + PARAMETER_CONTROLLER_ID, (req, res) -> this.saveCapabilities(req, res));
    }

    private String saveCapabilities(Request req, Response res) {
        String controllerId = req.params(PARAMETER_CONTROLLER_ID);

        this.logger.info("Storing capabilities of controller with ID '{}'.", controllerId);

        return null;
    }
}
