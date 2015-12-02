/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest;

import static io.logspace.hq.rest.HttpDateHelper.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;

import io.logspace.hq.core.api.orders.OrderService;
import io.logspace.hq.core.api.orders.StoredOrder;
import io.logspace.hq.rest.api.NotModifiedException;
import io.logspace.hq.rest.api.OrderNotFoundException;
import spark.Request;
import spark.Response;
import spark.Spark;

@Named
public class OrderResource extends AbstractSpaceResource {

    private static final String PARAMETER_CONTROLLER_ID = "controller-id";

    @Inject
    private OrderService orderService;

    @PostConstruct
    public void mount() {
        // mount a route without transformer, because we will simply stream the order directly from the service without parsing it
        Spark.get(this.resolvePath("/orders/:" + PARAMETER_CONTROLLER_ID), CONTENT_TYPE_JSON, (req, res) -> this.getOrder(req, res));
    }

    private String getOrder(Request req, Response res) throws IOException {
        this.validateSpace(req);

        String controllerId = req.params(PARAMETER_CONTROLLER_ID);

        StoredOrder storedOrder = this.orderService.getStoredOrder(controllerId);
        if (storedOrder == null) {
            throw OrderNotFoundException.forController(controllerId);
        }

        Date ifModifiedSince = parseHttpDate(req.headers(HEADER_IF_MODIFIED_SINCE));
        if (storedOrder.isNotModifiedSince(ifModifiedSince)) {
            throw new NotModifiedException();
        }

        this.logger.info("Serving order for AgentController with ID '{}'.", controllerId);
        try (InputStream inputStream = storedOrder.getInputStream()) {
            res.header(HEADER_LAST_MODIFIED, formatHttpDate(new Date(storedOrder.getLastModified())));
            return IOUtils.toString(inputStream, "UTF-8");
        }
    }
}
