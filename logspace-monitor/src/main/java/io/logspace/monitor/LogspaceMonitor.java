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

public final class LogspaceMonitor {

    private LogspaceMonitor() {
        // hide utility class constructor
    }

    public static void main(String[] args) {
        installAgents();

        runEndless();
    }

    private static void installAgents() {
        CpuAgent.create();
        DiskAgent.create();
        MemoryAgent.create();
        SwapAgent.create();
    }

    private static void runEndless() {
        while (true) {
            sleep();
        }
    }

    private static void sleep() {
        synchronized (LogspaceMonitor.class) {
            try {
                LogspaceMonitor.class.wait();
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }
}
