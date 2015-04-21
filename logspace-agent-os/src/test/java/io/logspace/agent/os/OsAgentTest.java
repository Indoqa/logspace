/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os;

import static org.junit.Assert.assertEquals;
import io.logspace.agent.impl.AgentControllerProvider;
import io.logspace.agent.impl.TestAgentController;

import org.junit.Test;

public class OsAgentTest {

    @Test
    public void test() {
        TestAgentController.installIfRequired("./target/test-events.json");
        TestAgentController agentController = (TestAgentController) AgentControllerProvider.getAgentController();

        assertEquals(0, agentController.getCollectedEvents().size());

        new CpuAgent("cpu-agent").execute(null);
        new DiskAgent("disk-agent").execute(null);
        new MemoryAgent("memory-agent").execute(null);
        new SwapAgent("swap-agent").execute(null);
        new SystemLoadAgent("system-load-agent").execute(null);

        assertEquals(5, agentController.getCollectedEvents().size());

        AgentControllerProvider.shutdown();
    }
}
