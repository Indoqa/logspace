/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import io.logspace.agent.api.order.AgentOrder;

public final class CpuAgent extends AbstractOsAgent {

    public static final String TYPE = "os/cpu";

    private CpuAgent() {
        super(TYPE);
    }

    public static CpuAgent create() {
        return new CpuAgent();
    }

    @Override
    public void execute(AgentOrder agentOrder) {
        OsEventBuilder eventBuilder = OsEventBuilder.createCpuBuilder(this.getEventBuilderData());

        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        eventBuilder.setProcessorCount(operatingSystemMXBean.getAvailableProcessors());
        eventBuilder.setSystemLoadAverage(operatingSystemMXBean.getSystemLoadAverage());

        if (operatingSystemMXBean instanceof com.sun.management.OperatingSystemMXBean) {
            eventBuilder.setSystemCpuLoad(((com.sun.management.OperatingSystemMXBean) operatingSystemMXBean).getSystemCpuLoad());
        }

        this.sendEvent(eventBuilder.toEvent());
    }
}
