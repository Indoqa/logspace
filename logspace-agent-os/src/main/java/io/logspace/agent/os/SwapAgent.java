/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

import io.logspace.agent.api.order.AgentOrder;

public final class SwapAgent extends AbstractOsAgent {

    public static final String TYPE = "os/swap";

    private SwapAgent() {
        super(TYPE);
    }

    public static SwapAgent create() {
        return new SwapAgent();
    }

    @Override
    public void execute(AgentOrder agentOrder) {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        if (operatingSystemMXBean == null) {
            return;
        }

        long totalSwapSpace = operatingSystemMXBean.getTotalSwapSpaceSize();
        long freeSwapSpace = operatingSystemMXBean.getFreeSwapSpaceSize();
        long usedSwapSpace = totalSwapSpace - freeSwapSpace;

        OsEventBuilder eventBuilder = OsEventBuilder.createSwapBuilder(this.getId(), this.getSystem(), this.getMarker());

        eventBuilder.setTotalSwapSpace(totalSwapSpace);
        eventBuilder.setFreeSwapSpace(freeSwapSpace);
        eventBuilder.setUsedSwapSpace(usedSwapSpace);

        this.sendEvent(eventBuilder.toEvent());
    }
}
