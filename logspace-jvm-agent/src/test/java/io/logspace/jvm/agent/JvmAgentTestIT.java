/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.jvm.agent;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.management.ManagementFactory;
import java.util.List;

import org.junit.Test;

import com.sun.tools.attach.VirtualMachine;

import io.logspace.agent.api.AgentControllerProvider;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.test.TestAgentController;

public class JvmAgentTestIT {

    private static final String JVM_IDENTIFIER = "agent-unit-tests";

    @Test
    public void test() {
        JvmAgent jvmAgent = Premain.getAgent();
        assertNull(jvmAgent);

        this.loadAgent();

        jvmAgent = Premain.getAgent();
        assertNotNull(jvmAgent);

        assertEquals(1, TestAgentController.getCollectedEvents().size());

        jvmAgent.execute(null);

        List<Event> collectedEvents = TestAgentController.getCollectedEvents();
        assertEquals(2, collectedEvents.size());

        Event event = collectedEvents.get(0);
        assertEquals("jvm/" + JVM_IDENTIFIER, event.getAgentId());
        assertEquals(Premain.getGlobalEventId(), event.getGlobalEventId());
        assertTrue("Expected at least 16 string properties, but received " + event.getStringProperties().size(),
            event.getStringProperties().size() >= 16);

        event = collectedEvents.get(1);
        assertEquals("jvm/" + JVM_IDENTIFIER, event.getAgentId());
        assertTrue("Expected at least 1 double property, but received " + event.getDoubleProperties().size(),
            event.getDoubleProperties().size() >= 1);
        assertEquals(4, event.getIntegerProperties().size());
        assertTrue("Expected at least 10 long properties, but received " + event.getLongProperties().size(),
            event.getLongProperties().size() >= 10);

        AgentControllerProvider.shutdown();
    }

    private String getDescriptionUrl() {
        return JvmAgentTestIT.class.getResource("/logspace-jvm-agent.json").toExternalForm();
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
            System.setProperty(JvmAgent.SYSTEM_PROPERTY_AGENT_DESCRIPTION_URL, this.getDescriptionUrl());
            vm.loadAgent(jvmAgentJarFile.getAbsolutePath(), "");
            vm.detach();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
