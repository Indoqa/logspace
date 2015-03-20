/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp.resource;

import io.logspace.agent.api.event.Optional;
import io.logspace.agent.api.order.AgentControllerOrder;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;

import com.indoqa.spark.AbstractJsonResourcesBase;

@Named
public class OrdersResource extends AbstractJsonResourcesBase {

    private static final String PARAMETER_CONTROLLER_ID = "controller-id";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void mount() {
        this.get("/orders/:" + PARAMETER_CONTROLLER_ID, (req, res) -> this.getOrder(req, res));
    }

    private AgentControllerOrder getOrder(Request req, Response res) {
        String controllerId = req.params(PARAMETER_CONTROLLER_ID);
        this.logger.debug("Retrieving order for controller " + controllerId);

        AgentControllerOrder result = new AgentControllerOrder();

        result.setCommitMaxCount(Optional.of(1000));
        result.setCommitMaxSeconds(Optional.of(60));

        AgentOrder agentOrder = new AgentOrder();
        agentOrder.setId("TEST-AGENT-001");
        agentOrder.setTriggerType(TriggerType.Cron);
        agentOrder.setTriggerParameter(Optional.of("0/10 * * * * ? *"));
        result.add(agentOrder);

        return result;
    }
}