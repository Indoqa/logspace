/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.monitor;

import io.logspace.agent.os.CpuAgent;
import io.logspace.agent.os.DiskAgent;
import io.logspace.agent.os.MemoryAgent;
import io.logspace.agent.os.SwapAgent;
import io.logspace.agent.os.SystemLoadAgent;

public final class LogspaceMonitor {

    public static final String SYSTEM_PROPERTY_SYSTEM_IDENTIFIER = "io.logspace.system-identifier";

    private LogspaceMonitor() {
        // hide utility class constructor
    }

    public static void main(String[] args) {
        String systemIdentifier = getSystemIdentifier();
        installAgents(systemIdentifier);

        runEndless();
    }

    private static String getSystemIdentifier() {
        String jvmIdentifier = System.getProperty(SYSTEM_PROPERTY_SYSTEM_IDENTIFIER);

        if (jvmIdentifier == null || jvmIdentifier.isEmpty()) {
            throw new IllegalArgumentException("System Property: '" + SYSTEM_PROPERTY_SYSTEM_IDENTIFIER
                    + "' not defined. Please set this property to a unique value.");
        }

        return jvmIdentifier;
    }

    private static void installAgents(String systemIdentifier) {
        CpuAgent.create(systemIdentifier + "/cpu");
        DiskAgent.create(systemIdentifier + "/disk");
        MemoryAgent.create(systemIdentifier + "/memory");
        SwapAgent.create(systemIdentifier + "/swap");
        SystemLoadAgent.create(systemIdentifier + "-system-load");
    }

    private static void runEndless() {
        while (true) {
            sleepSilent(10000);
        }
    }

    private static void sleepSilent(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
