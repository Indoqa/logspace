package io.logspace.agent.api;

import io.logspace.agent.api.eventrequest.HqEventRequest;
import io.logspace.agent.api.order.AgentCapabilities;

public interface Agent {

    AgentCapabilities provideCapabilities();

    void receiveEventRequest(HqEventRequest eventRequest);

    void setAgentController(AgentController agentController);

}
