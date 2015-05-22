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

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public final class SystemLoadAgent extends AbstractAgent {

    private SystemLoadAgent(String agentId) {
        super(agentId, "os/system-load", TriggerType.Off, TriggerType.Cron);
    }

    public static SystemLoadAgent create(String agentId) {
        return new SystemLoadAgent(agentId);
    }

    @Override
    public void execute(AgentOrder agentOrder) {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();

        OsEventBuilder eventBuilder = OsEventBuilder.createCpuBuilder(this.getId(), this.getSystem()).setSystemLoadAverage(
                operatingSystemMXBean.getSystemLoadAverage());

        this.addAdditionalProperties(eventBuilder);

        this.sendEvent(eventBuilder.toEvent());
    }

    private void addAdditionalProperties(OsEventBuilder eventBuilder) {
        com.sun.management.OperatingSystemMXBean operatingSystemMXBean = ManagementFactory
                .getPlatformMXBean(com.sun.management.OperatingSystemMXBean.class);
        if (operatingSystemMXBean == null) {
            return;
        }

        eventBuilder.setSystemCpuLoad(operatingSystemMXBean.getSystemCpuLoad());
    }
}
