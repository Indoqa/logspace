/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import io.logspace.agent.api.eventrequest.HqEventRequest;
import io.logspace.agent.api.order.AgentCapabilities;

public interface Agent {

    AgentCapabilities provideCapabilities();

    void receiveEventRequest(HqEventRequest eventRequest);

    void setAgentController(AgentController agentController);

}
