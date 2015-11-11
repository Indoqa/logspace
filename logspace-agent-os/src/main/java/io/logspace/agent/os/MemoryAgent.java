/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os;

import static io.logspace.agent.os.OsEventBuilder.createMemoryBuilder;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import io.logspace.agent.api.order.AgentOrder;

public final class MemoryAgent extends AbstractOsAgent {

    public static final String TYPE = "os/memory";

    private MemoryAgent() {
        super(TYPE);
    }

    public static MemoryAgent create() {
        return new MemoryAgent();
    }

    @Override
    public void execute(AgentOrder agentOrder) {
        OperatingSystemMXBean operatingSystemBean = ManagementFactory.getOperatingSystemMXBean();

        if (!(operatingSystemBean instanceof com.sun.management.OperatingSystemMXBean)) {
            return;
        }

        com.sun.management.OperatingSystemMXBean operatingSystem = (com.sun.management.OperatingSystemMXBean) operatingSystemBean;

        OsEventBuilder osEventBuilder = createMemoryBuilder(this.getId(), this.getSystem(), this.getMarker());

        osEventBuilder.setTotalMemory(operatingSystem.getTotalPhysicalMemorySize());
        osEventBuilder.setFreeMemory(operatingSystem.getFreePhysicalMemorySize());
        osEventBuilder.setUsedMemory(operatingSystem.getTotalPhysicalMemorySize() - operatingSystem.getFreePhysicalMemorySize());
        osEventBuilder.setCommittedVirtualMemory(operatingSystem.getCommittedVirtualMemorySize());

        this.sendEvent(osEventBuilder.toEvent());
    }
}
