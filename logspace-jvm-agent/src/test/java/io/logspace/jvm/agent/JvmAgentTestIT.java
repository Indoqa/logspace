/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.jvm.agent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import io.logspace.agent.api.AgentControllerProvider;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.test.TestAgentController;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.management.ManagementFactory;
import java.util.List;

import org.junit.Test;

import com.sun.tools.attach.VirtualMachine;

public class JvmAgentTestIT {

    private static final String JVM_IDENTIFIER = "agent-unit-tests";

    @Test
    public void test() {
        TestAgentController.installIfRequired("./target/test-events.json");
        TestAgentController agentController = (TestAgentController) AgentControllerProvider.getAgentController();

        assertEquals(0, agentController.getCollectedEvents().size());

        JvmAgent jvmAgent = Premain.getAgent();
        assertNull(jvmAgent);

        this.loadAgent();

        jvmAgent = Premain.getAgent();
        assertNotNull(jvmAgent);

        assertEquals(0, agentController.getCollectedEvents().size());

        jvmAgent.execute(null);

        List<Event> collectedEvents = agentController.getCollectedEvents();
        assertEquals(1, collectedEvents.size());

        Event event = collectedEvents.get(0);
        assertEquals("jvm-" + JVM_IDENTIFIER, event.getAgentId());
        assertTrue("Expected at least 1 double property, but received " + event.getDoubleProperties().size(), event
                .getDoubleProperties().size() >= 1);
        assertEquals(4, event.getIntegerProperties().size());
        assertTrue("Expected at least 10 long properties, but received " + event.getLongProperties().size(), event.getLongProperties()
                .size() >= 10);

        AgentControllerProvider.shutdown();
    }

    private File getJvmAgentJarFile() {
        File target = new File("./target");

        File[] listFiles = target.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File file, String name) {
                return name.matches("logspace-jvm-agent-\\d+\\.\\d+\\.\\d+(-SNAPSHOT)?.jar");
            }
        });

        return listFiles[0];
    }

    private void loadAgent() {
        File jvmAgentJarFile = this.getJvmAgentJarFile();

        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        String pid = nameOfRunningVM.substring(0, nameOfRunningVM.indexOf('@'));

        try {
            VirtualMachine vm = VirtualMachine.attach(pid);
            System.setProperty(JvmAgent.SYSTEM_PROPERTY_JVM_IDENTIFIER, JVM_IDENTIFIER);
            vm.loadAgent(jvmAgentJarFile.getAbsolutePath(), "");
            vm.detach();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
