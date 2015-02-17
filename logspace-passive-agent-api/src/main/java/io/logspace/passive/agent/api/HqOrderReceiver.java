package io.logspace.passive.agent.api;

import io.logspace.passive.agent.api.order.AgentCapabilities;
import io.logspace.passive.agent.api.order.HqOrders;

public interface HqOrderReceiver {

    AgentCapabilities provideCapabilities();

    void receiveHqOrder(HqOrders hqOrder);

}
