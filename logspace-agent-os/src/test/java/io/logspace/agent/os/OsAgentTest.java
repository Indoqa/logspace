/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import io.logspace.agent.api.AgentControllerProvider;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.test.TestAgentController;

public class OsAgentTest {

    @SuppressWarnings("unused")
    private static int getRootDirectoryCount() {
        int result = 0;

        for (Path eachRootDirectory : FileSystems.getDefault().getRootDirectories()) {
            try {
                FileStore store = Files.getFileStore(eachRootDirectory);
                result++;
            } catch (IOException e) {
                // root is not available, do not count it
            }
        }

        return result;
    }

    private static Collection<Event> removeEvents(Collection<Event> events, String type) {
        List<Event> result = new ArrayList<Event>();

        for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {
            Event eachEvent = iterator.next();

            if (type.equals(eachEvent.getType())) {
                result.add(eachEvent);
                iterator.remove();
            }
        }

        return result;
    }

    @Test
    public void test() {
        TestAgentController.installIfRequired("./target/test-events.json");

        assertEquals(0, TestAgentController.getCollectedEventCount());

        CpuAgent.create().execute();
        DiskAgent.create().execute();
        MemoryAgent.create().execute();
        SwapAgent.create().execute();

        Collection<Event> cpuEvents = removeEvents(TestAgentController.getCollectedEvents(), CpuAgent.TYPE);
        assertEquals("Expected one CPU event.", 1, cpuEvents.size());

        Collection<Event> diskEvents = removeEvents(TestAgentController.getCollectedEvents(), DiskAgent.TYPE);
        assertEquals("Expected one disk event per file-system root.", getRootDirectoryCount(), diskEvents.size());

        Collection<Event> memoryEvents = removeEvents(TestAgentController.getCollectedEvents(), MemoryAgent.TYPE);
        assertEquals("Expected one memory event.", 1, memoryEvents.size());

        Collection<Event> swapEvents = removeEvents(TestAgentController.getCollectedEvents(), SwapAgent.TYPE);
        assertEquals("Expected one swap event.", 1, swapEvents.size());

        assertEquals("Found unexpected events.", 0, TestAgentController.getCollectedEventCount());

        AgentControllerProvider.shutdown();
    }
}
