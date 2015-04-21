/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.jvm.agent;

import io.logspace.agent.api.event.AbstractEventBuilder;
import io.logspace.agent.api.event.Optional;

public class JvmEventBuilder extends AbstractEventBuilder {

    public static final String PROPERTY_JVM_IDENTIFIER = "jvm-identifier";

    public static final String PROPERTY_INITIAL_HEAP_MEMORY = "initial-heap-memory";
    public static final String PROPERTY_MAX_HEAP_MEMORY = "max-heap-memory";
    public static final String PROPERTY_USED_HEAP_MEMORY = "used-heap-memory";
    public static final String PROPERTY_COMMITED_HEAP_MEMORY = "commited-heap-memory";

    public static final String PROPERTY_INITIAL_NON_HEAP_MEMORY = "initial-non-heap-memory";
    public static final String PROPERTY_MAX_NON_HEAP_MEMORY = "max-non-heap-memory";
    public static final String PROPERTY_USED_NON_HEAP_MEMORY = "used-non-heap-memory";
    public static final String PROPERTY_COMMITED_NON_HEAP_MEMORY = "commited-non-heap-memory";

    public static final String PROPERTY_OBJECT_PENDING_FINALIZATION_COUNT = "object-pending-finalization-count";

    public static final String PROPERTY_LOADED_CLASS_COUNT = "loaded-class-count";
    public static final String PROPERTY_TOTAL_LOADED_CLASS_COUNT = "total-loaded-class-count";
    public static final String PROPERTY_UNLOADED_CLASS_COUNT = "unloaded-class-count";

    public static final String PROPERTY_THREAD_COUNT = "thread-count";
    public static final String PROPERTY_DAEMON_THREAD_COUNT = "daemon-thread-count";

    public static final String PROPERTY_GARBAGE_COLLECTOR_RUN_COUNT = "garbagecollector-run-count";
    public static final String PROPERTY_GARBAGE_COLLECTOR_TIME = "garbagecollector-time";

    public static final String PROPERTY_OPEN_FILE_DESCRIPTOR_COUNT = "open-file-descriptor-count";
    public static final String PROPERTY_MAX_FILE_DESCRIPTOR_COUNT = "max-file-descriptor-count";

    public static final String PROPERTY_SYSTEM_CPU_LOAD = "system-cpu-load";
    public static final String PROPERTY_SYSTEM_LOAD_AVERAGE = "system-load-average";

    private static final Optional<String> JVM_EVENT_TYPE = Optional.of("jvm");

    private Optional<String> eventType;

    private JvmEventBuilder(String agentId, Optional<String> eventType) {
        super(agentId);

        this.eventType = eventType;
    }

    public static JvmEventBuilder createJvmBuilder(String agentId) {
        return new JvmEventBuilder(agentId, JVM_EVENT_TYPE);
    }

    public void setCommitedHeapMemory(long commitedHeapMemory) {
        this.addProperty(PROPERTY_COMMITED_HEAP_MEMORY, commitedHeapMemory);
    }

    public void setCommitedNonHeapMemory(long commitedNonHeapMemory) {
        this.addProperty(PROPERTY_COMMITED_NON_HEAP_MEMORY, commitedNonHeapMemory);
    }

    public void setDaemonThreadCount(int daemonThreadCount) {
        this.addProperty(PROPERTY_DAEMON_THREAD_COUNT, daemonThreadCount);
    }

    public void setGarbageCollectorRunCount(String garbageCollectorName, long runCount) {
        this.addProperty(this.normalizeName(garbageCollectorName) + "-" + PROPERTY_GARBAGE_COLLECTOR_RUN_COUNT, runCount);
    }

    public void setGarbageCollectorTime(String garbageCollectorName, long collectionTime) {
        this.addProperty(this.normalizeName(garbageCollectorName) + "-" + PROPERTY_GARBAGE_COLLECTOR_TIME, collectionTime);
    }

    public void setInitialHeapMemory(long initialHeapMemory) {
        this.addProperty(PROPERTY_INITIAL_HEAP_MEMORY, initialHeapMemory);
    }

    public void setInitialNonHeapMemory(long initialNonHeapMemory) {
        this.addProperty(PROPERTY_INITIAL_NON_HEAP_MEMORY, initialNonHeapMemory);
    }

    public void setLoadedClassCount(int loadedClassCount) {
        this.addProperty(PROPERTY_LOADED_CLASS_COUNT, loadedClassCount);
    }

    public void setMaxFileDescriptorCount(long maxFileDescriptorCount) {
        this.addProperty(PROPERTY_MAX_FILE_DESCRIPTOR_COUNT, maxFileDescriptorCount);
    }

    public void setMaxHeapMemory(long maxHeapMemory) {
        this.addProperty(PROPERTY_MAX_HEAP_MEMORY, maxHeapMemory);
    }

    public void setMaxNonHeapMemory(long maxNonHeapMemory) {
        this.addProperty(PROPERTY_MAX_NON_HEAP_MEMORY, maxNonHeapMemory);
    }

    public void setObjectPendingFinalizationCount(int objectPendingFinalizationCount) {
        this.addProperty(PROPERTY_OBJECT_PENDING_FINALIZATION_COUNT, objectPendingFinalizationCount);
    }

    public void setOpenFileDescriptorCount(long openFileDescriptorCount) {
        this.addProperty(PROPERTY_OPEN_FILE_DESCRIPTOR_COUNT, openFileDescriptorCount);
    }

    public void setSystemCpuLoad(double systemCpuLoad) {
        this.addProperty(PROPERTY_SYSTEM_CPU_LOAD, systemCpuLoad);
    }

    public void setSystemLoadAverage(double systemLoadAverage) {
        this.addProperty(PROPERTY_SYSTEM_LOAD_AVERAGE, systemLoadAverage);
    }

    public void setThreadCount(int threadCount) {
        this.addProperty(PROPERTY_THREAD_COUNT, threadCount);
    }

    public void setTotalLoadedClassCount(long totalLoadedClassCount) {
        this.addProperty(PROPERTY_TOTAL_LOADED_CLASS_COUNT, totalLoadedClassCount);
    }

    public void setUnloadedClassCount(long unloadedClassCount) {
        this.addProperty(PROPERTY_UNLOADED_CLASS_COUNT, unloadedClassCount);
    }

    public void setUsedHeapMemory(long usedHeapMemory) {
        this.addProperty(PROPERTY_USED_HEAP_MEMORY, usedHeapMemory);
    }

    public void setUsedNonHeapMemory(long usedNonHeapMemory) {
        this.addProperty(PROPERTY_USED_NON_HEAP_MEMORY, usedNonHeapMemory);
    }

    @Override
    protected Optional<String> getType() {
        return this.eventType;
    }

    private String normalizeName(String name) {
        return name.toLowerCase().replace(' ', '-');
    }
}
