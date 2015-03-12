/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os;

import io.logspace.agent.api.Agent;
import io.logspace.agent.api.AgentController;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.eventrequest.HqEventRequest;
import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.os.api.CpuEventBuilder;

public class CpuAgent implements Agent {

    private AgentController agentController;

    @Override
    public AgentCapabilities provideCapabilities() {
        return null;
    }

    @Override
    public void receiveEventRequest(HqEventRequest eventRequest) {
        // nothing to do
    }

    @Override
    public void setAgentController(AgentController passiveController) {
        this.agentController = passiveController;
    }

    public void someMethodSendingAnEvent() {
        Event event = new CpuEventBuilder().setLoadAverage(3.2).toEvent();
        this.agentController.send(event);
    }
}
