/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.orders;

import static io.logspace.hq.rest.HttpDateHelper.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import io.logspace.hq.core.api.orders.Order;
import io.logspace.hq.core.api.orders.OrderService;
import io.logspace.hq.rest.AbstractSpaceResource;
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

    private static boolean isUnmodified(Date ifModifiedSince, Date lastModified) {
        if (ifModifiedSince == null) {
            return false;
        }

        // just compare down to seconds because the date format used does not include the milliseconds
        return MILLISECONDS.toSeconds(lastModified.getTime()) == MILLISECONDS.toSeconds(ifModifiedSince.getTime());
    }

    @PostConstruct
    public void mount() {
        // mount a route without transformer, because we will simply return the content the order directly without parsing it
        Spark.get(this.resolvePath("/orders/:" + PARAMETER_CONTROLLER_ID), CONTENT_TYPE_JSON, (req, res) -> this.getOrder(req, res));
    }

    private String getOrder(Request req, Response res) {
        this.validateSpace(req);

        String controllerId = req.params(PARAMETER_CONTROLLER_ID);

        Order order = this.orderService.getOrder(controllerId);
        if (order == null) {
            throw OrderNotFoundException.forController(controllerId);
        }

        Date ifModifiedSince = parseHttpDate(req.headers(HEADER_IF_MODIFIED_SINCE));
        if (isUnmodified(ifModifiedSince, order.getLastModified())) {
            throw new NotModifiedException();
        }

        this.logger.info("Serving order for AgentController with ID '{}'.", controllerId);
        res.header(HEADER_LAST_MODIFIED, formatHttpDate(order.getLastModified()));
        return order.getContent();
    }
}
