/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import io.logspace.agent.api.json.AgentControllerCapabilitiesJsonDeserializer;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.hq.core.api.capabilities.CapabilitiesService;
import io.logspace.hq.rest.api.InvalidControllerIdException;
import spark.Request;

@Named
public class CapabilitiesResource extends AbstractSpaceResource {

    private static final String PARAMETER_CONTROLLER_ID = "controller-id";

    @Inject
    private CapabilitiesService capabilitiesService;

    @PostConstruct
    public void mount() {
        this.put("/capabilities/:" + PARAMETER_CONTROLLER_ID, (req, res) -> this.saveCapabilities(req));
    }

    private Void saveCapabilities(Request req) throws IOException {
        String space = this.getSpace(req);

        String controllerId = req.params(PARAMETER_CONTROLLER_ID);
        this.logger.info("Storing capabilities of controller with ID '{}'.", controllerId);

        AgentControllerCapabilities agentControllerCapabilities = AgentControllerCapabilitiesJsonDeserializer
            .fromJson(req.bodyAsBytes());
        agentControllerCapabilities.setSpace(space);

        if (!controllerId.equals(agentControllerCapabilities.getId())) {
            throw new InvalidControllerIdException("The ID in the path does not match the ID in the payload.");
        }

        this.capabilitiesService.save(agentControllerCapabilities);

        return null;
    }
}
