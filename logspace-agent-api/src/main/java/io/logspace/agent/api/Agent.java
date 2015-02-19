package io.logspace.agent.api;

import io.logspace.agent.api.eventrequest.HqEventRequest;
import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.api.order.HqOrders;

public interface Agent {

    AgentCapabilities provideCapabilities();

    void receiveEventRequest(HqEventRequest eventRequest);

    void receiveHqOrder(HqOrders hqOrder);

    void setAgentController(AgentController agentController);

}
