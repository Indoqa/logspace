/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os;

import io.logspace.agent.api.event.AbstractEventBuilder;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.Optional;

public final class OsEventBuilder extends AbstractEventBuilder {

    public static final String PROPERTY_PROCESSOR_COUNT = "processor_count";

    public static final String PROPERTY_SYSTEM_CPU_LOAD = "system_cpu_load";
    public static final String PROPERTY_SYSTEM_LOAD_AVERAGE = "system_load_average";

    private static final String PROPERTY_TOTAL_MEMORY = "total_memory";
    private static final String PROPERTY_FREE_MEMORY = "free_memory";
    private static final String PROPERTY_USED_MEMORY = "used_memory";
    private static final String PROPERTY_COMMITTED_VIRTUAL_MEMORY = "committed_virtual_memory";

    private static final String PROPERTY_DISK_PATH = "disk_path";
    private static final String PROPERTY_DISK_TOTAL_SPACE = "disk_total_space";
    private static final String PROPERTY_DISK_UNALLOCATED_SPACE = "disk_unallocated_space";
    private static final String PROPERTY_DISK_USED_SPACE = "disk_used_space";
    private static final String PROPERTY_DISK_USABLE_SPACE = "disk_usable_space";

    private static final String PROPERTY_SWAP_FREE_SPACE = "swap_free_space";
    private static final String PROPERTY_SWAP_TOTAL_SPACE = "swap_total_space";
    private static final String PROPERTY_SWAP_USED_SPACE = "swap_used_space";

    private static final Optional<String> MEMORY_EVENT_TYPE = Optional.of("os/memory");
    private static final Optional<String> CPU_EVENT_TYPE = Optional.of("os/cpu");
    private static final Optional<String> SYSTEM_LOAD_EVENT_TYPE = Optional.of("os/system_load");
    private static final Optional<String> SWAP_EVENT_TYPE = Optional.of("os/swap");
    private static final Optional<String> DISK_EVENT_TYPE = Optional.of("os/disk");

    private Optional<String> eventType;

    private OsEventBuilder(String agentId, String system, Event parentEvent) {
        super(agentId, system, parentEvent);
    }

    private OsEventBuilder(String agentId, String system, Optional<String> eventType) {
        super(agentId, system);

        this.eventType = eventType;
    }

    public static OsEventBuilder createCpuBuilder(String agentId, String system) {
        return new OsEventBuilder(agentId, system, CPU_EVENT_TYPE);
    }

    public static OsEventBuilder createDiskBuilder(String agentId, String system) {
        return new OsEventBuilder(agentId, system, DISK_EVENT_TYPE);
    }

    public static OsEventBuilder createMemoryBuilder(String agentId, String system) {
        return new OsEventBuilder(agentId, system, MEMORY_EVENT_TYPE);
    }

    public static OsEventBuilder createSwapBuilder(String agentId, String system) {
        return new OsEventBuilder(agentId, system, SWAP_EVENT_TYPE);
    }

    public static OsEventBuilder createSystemLoadBuilder(String agentId, String system) {
        return new OsEventBuilder(agentId, system, SYSTEM_LOAD_EVENT_TYPE);
    }

    public OsEventBuilder setCommittedVirtualMemory(long maxMemory) {
        this.addProperty(PROPERTY_COMMITTED_VIRTUAL_MEMORY, maxMemory);
        return this;
    }

    public OsEventBuilder setDiskPath(String diskPath) {
        this.addProperty(PROPERTY_DISK_PATH, diskPath);
        return this;
    }

    public OsEventBuilder setFreeMemory(long freeMemory) {
        this.addProperty(PROPERTY_FREE_MEMORY, freeMemory);
        return this;
    }

    public OsEventBuilder setFreeSwapSpace(long freeSwapSpace) {
        this.addProperty(PROPERTY_SWAP_FREE_SPACE, freeSwapSpace);
        return this;
    }

    public OsEventBuilder setProcessorCount(int processorCount) {
        this.addProperty(PROPERTY_PROCESSOR_COUNT, processorCount);
        return this;
    }

    public OsEventBuilder setSystemCpuLoad(double systemCpuLoad) {
        this.addProperty(PROPERTY_SYSTEM_CPU_LOAD, systemCpuLoad);
        return this;
    }

    public OsEventBuilder setSystemLoadAverage(double systemLoadAverage) {
        this.addProperty(PROPERTY_SYSTEM_LOAD_AVERAGE, systemLoadAverage);
        return this;
    }

    public OsEventBuilder setTotalDiskSpace(long totalDiskSpace) {
        this.addProperty(PROPERTY_DISK_TOTAL_SPACE, totalDiskSpace);
        return this;
    }

    public OsEventBuilder setTotalMemory(long totalMemory) {
        this.addProperty(PROPERTY_TOTAL_MEMORY, totalMemory);
        return this;
    }

    public OsEventBuilder setTotalSwapSpace(long totalSwapSpace) {
        this.addProperty(PROPERTY_SWAP_TOTAL_SPACE, totalSwapSpace);
        return this;
    }

    public OsEventBuilder setUnallocatedDiskSpace(long unallocatedDiskSpace) {
        this.addProperty(PROPERTY_DISK_UNALLOCATED_SPACE, unallocatedDiskSpace);
        return this;
    }

    public OsEventBuilder setUsableDiskSpace(long usableDiskSpace) {
        this.addProperty(PROPERTY_DISK_USABLE_SPACE, usableDiskSpace);
        return this;
    }

    public OsEventBuilder setUsedDiskSpace(long usedDiskSpace) {
        this.addProperty(PROPERTY_DISK_USED_SPACE, usedDiskSpace);
        return this;
    }

    public OsEventBuilder setUsedMemory(long usedMemory) {
        this.addProperty(PROPERTY_USED_MEMORY, usedMemory);
        return this;
    }

    public OsEventBuilder setUsedSwapSpace(long usedSwapSpace) {
        this.addProperty(PROPERTY_SWAP_USED_SPACE, usedSwapSpace);
        return this;
    }

    /**
     * @see io.logspace.agent.api.event.AbstractEventBuilder#getType()
     */
    @Override
    protected Optional<String> getType() {
        return this.eventType;
    }
}