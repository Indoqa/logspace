/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os;

import static io.logspace.agent.os.api.OsEventBuilder.createMemoryBuilder;
import io.logspace.agent.api.AbstractAgent;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;
import io.logspace.agent.impl.AgentControllerProvider;

public class MemoryAgent extends AbstractAgent {

    private MemoryAgent(String agentId) {
        super(agentId, "os/memory", TriggerType.Off, TriggerType.Cron);

        this.setAgentController(AgentControllerProvider.getAgentController());
    }

    public static MemoryAgent create(String agentId) {
        return new MemoryAgent(agentId);
    }

    @Override
    public void execute(AgentOrder agentOrder) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;

        Event event = createMemoryBuilder(this.getId(), this.getSystem()).setMaxMemory(maxMemory).setTotalMemory(totalMemory)
                .setFreeMemory(freeMemory).setUsedMemory(usedMemory).toEvent();
        this.sendEvent(event);
    }
}
