/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os;

import io.logspace.agent.api.AbstractAgent;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;
import io.logspace.agent.impl.AgentControllerProvider;
import io.logspace.agent.os.api.OsEventBuilder;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public class CpuAgent extends AbstractAgent {

    private CpuAgent(String agentId) {
        super(agentId, "os/cpu", TriggerType.Off, TriggerType.Cron);

        this.setAgentController(AgentControllerProvider.getAgentController());
    }

    public static CpuAgent create(String agentId) {
        return new CpuAgent(agentId);
    }

    @Override
    public void execute(AgentOrder agentOrder) {
        OsEventBuilder eventBuilder = OsEventBuilder.createCpuBuilder(this.getId()).setProcessorCount(
                Runtime.getRuntime().availableProcessors());

        this.addAdditionalProperties(eventBuilder);

        this.sendEvent(eventBuilder.toEvent());
    }

    private void addAdditionalProperties(OsEventBuilder eventBuilder) {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        if (operatingSystemMXBean == null) {
            return;
        }

        eventBuilder.setProcessCpuLoad(operatingSystemMXBean.getProcessCpuLoad());
        eventBuilder.setProcessCpuTime(operatingSystemMXBean.getProcessCpuTime());
    }
}
