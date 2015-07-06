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

public final class JvmEventBuilder extends AbstractEventBuilder {

    public static final String PROPERTY_JVM_IDENTIFIER = "jvm-identifier";

    public static final String PROPERTY_INITIAL_HEAP_MEMORY = "initial_heap_memory";
    public static final String PROPERTY_MAX_HEAP_MEMORY = "max_heap_memory";
    public static final String PROPERTY_USED_HEAP_MEMORY = "used_heap_memory";
    public static final String PROPERTY_COMMITED_HEAP_MEMORY = "commited_heap_memory";

    public static final String PROPERTY_INITIAL_NON_HEAP_MEMORY = "initial_non_heap_memory";
    public static final String PROPERTY_MAX_NON_HEAP_MEMORY = "max_non_heap_memory";
    public static final String PROPERTY_USED_NON_HEAP_MEMORY = "used_non_heap_memory";
    public static final String PROPERTY_COMMITED_NON_HEAP_MEMORY = "commited_non_heap_memory";

    public static final String PROPERTY_OBJECT_PENDING_FINALIZATION_COUNT = "object_pending_finalization_count";

    public static final String PROPERTY_LOADED_CLASS_COUNT = "loaded_class_count";
    public static final String PROPERTY_TOTAL_LOADED_CLASS_COUNT = "total_loaded_class_count";
    public static final String PROPERTY_UNLOADED_CLASS_COUNT = "unloaded_class_count";

    public static final String PROPERTY_THREAD_COUNT = "thread_count";
    public static final String PROPERTY_DAEMON_THREAD_COUNT = "daemon_thread_count";

    public static final String PROPERTY_GARBAGE_COLLECTOR_RUN_COUNT = "garbagecollector_run_count";
    public static final String PROPERTY_GARBAGE_COLLECTOR_TIME = "garbagecollector_time";

    public static final String PROPERTY_OPEN_FILE_DESCRIPTOR_COUNT = "open_file_descriptor_count";
    public static final String PROPERTY_MAX_FILE_DESCRIPTOR_COUNT = "max_file_descriptor_count";

    public static final String PROPERTY_PROCESS_CPU_LOAD = "process_cpu_load";
    public static final String PROPERTY_PROCESS_CPU_TIME = "process_cpu_time";

    public static final String PROPERTY_AVAILABLE_PROCESSORS = "available_processors";
    public static final String PROPERTY_CPU_ENDIAN = "cpu_endian";

    private static final Optional<String> JVM_EVENT_TYPE = Optional.of("jvm/statistics");

    private static final Optional<String> JVM_START_EVENT_TYPE = Optional.of("jvm/start");
    private static final Optional<String> JVM_STOP_EVENT_TYPE = Optional.of("jvm/stop");

    private static final Optional<String> JVM_AGENT_ATTACHED_EVENT_TYPE = Optional.of("jvm/agent-attached");

    private static final String PROPERTY_JAVA_RUNTIME_NAME = "java_runtime_name";
    private static final String PROPERTY_JAVA_RUNTIME_VERSION = "java_runtime_version";

    private static final String PROPERTY_JVM_INFO = "jvm_info";
    private static final String PROPERTY_JVM_NAME = "jvm_name";
    private static final String PROPERTY_JVM_VENDOR = "jvm_vendor";
    private static final String PROPERTY_JVM_VERSION = "jvm_version";

    private static final String PROPERTY_OS_ARCHITECTURE = "os_architecture";
    private static final String PROPERTY_OS_NAME = "os_name";
    private static final String PROPERTY_OS_VERSION = "os_version";

    private static final String PROPERTY_USER_COUNTRY = "user_country";
    private static final String PROPERTY_USER_DIRECTORY = "user_directory";
    private static final String PROPERTY_USER_HOME = "user_home";
    private static final String PROPERTY_USER_LANGUAGE = "user_langugage";
    private static final String PROPERTY_USER_NAME = "user_name";
    private static final String PROPERTY_USER_TIMEZONE = "user_timezone";

    private Optional<String> eventType;

    private JvmEventBuilder(String agentId, String system, Optional<String> eventType) {
        super(agentId, system);

        this.eventType = eventType;
    }

    public static JvmEventBuilder createJvmAgentAttachedBuilder(String agentId, String system, String globalEventId) {
        JvmEventBuilder jvmEventBuilder = new JvmEventBuilder(agentId, system, JVM_AGENT_ATTACHED_EVENT_TYPE);
        jvmEventBuilder.setGlobalEventId(globalEventId);
        return jvmEventBuilder;
    }

    public static JvmEventBuilder createJvmBuilder(String agentId, String system) {
        return new JvmEventBuilder(agentId, system, JVM_EVENT_TYPE);
    }

    public static JvmEventBuilder createJvmStartBuilder(String agentId, String system, String globalEventId) {
        JvmEventBuilder jvmEventBuilder = new JvmEventBuilder(agentId, system, JVM_START_EVENT_TYPE);
        jvmEventBuilder.setGlobalEventId(globalEventId);
        return jvmEventBuilder;
    }

    public static JvmEventBuilder createJvmStopBuilder(String agentId, String system, String globalEventId) {
        JvmEventBuilder jvmEventBuilder = new JvmEventBuilder(agentId, system, JVM_STOP_EVENT_TYPE);
        jvmEventBuilder.setGlobalEventId(globalEventId);
        return jvmEventBuilder;
    }

    public void setAvailableProcessors(int availableProcessors) {
        this.addProperty(PROPERTY_AVAILABLE_PROCESSORS, availableProcessors);
    }

    public void setCommitedHeapMemory(long commitedHeapMemory) {
        this.addProperty(PROPERTY_COMMITED_HEAP_MEMORY, commitedHeapMemory);
    }

    public void setCommitedNonHeapMemory(long commitedNonHeapMemory) {
        this.addProperty(PROPERTY_COMMITED_NON_HEAP_MEMORY, commitedNonHeapMemory);
    }

    public void setCpuEndian(String cpuEndian) {
        this.addProperty(PROPERTY_CPU_ENDIAN, cpuEndian);
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

    public void setJavaRuntimeName(String javaRuntimeName) {
        this.addProperty(PROPERTY_JAVA_RUNTIME_NAME, javaRuntimeName);
    }

    public void setJavaRuntimeVersion(String javaRuntimeVersion) {
        this.addProperty(PROPERTY_JAVA_RUNTIME_VERSION, javaRuntimeVersion);
    }

    public void setJvmInfo(String jvmInfo) {
        this.addProperty(PROPERTY_JVM_INFO, jvmInfo);
    }

    public void setJvmName(String jvmName) {
        this.addProperty(PROPERTY_JVM_NAME, jvmName);
    }

    public void setJvmVendor(String jvmVendor) {
        this.addProperty(PROPERTY_JVM_VENDOR, jvmVendor);
    }

    public void setJvmVersion(String jvmVersion) {
        this.addProperty(PROPERTY_JVM_VERSION, jvmVersion);
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

    public void setOsArchitecture(String osArchitecture) {
        this.addProperty(PROPERTY_OS_ARCHITECTURE, osArchitecture);
    }

    public void setOsName(String osName) {
        this.addProperty(PROPERTY_OS_NAME, osName);
    }

    public void setOsVersion(String osVersion) {
        this.addProperty(PROPERTY_OS_VERSION, osVersion);
    }

    public void setProcessCpuLoad(double processCpuLoad) {
        this.addProperty(PROPERTY_PROCESS_CPU_LOAD, processCpuLoad);
    }

    public void setProcessCpuTime(double processCpuTime) {
        this.addProperty(PROPERTY_PROCESS_CPU_TIME, processCpuTime);
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

    public void setUserCountry(String userCountry) {
        this.addProperty(PROPERTY_USER_COUNTRY, userCountry);
    }

    public void setUserDirectory(String userDirectory) {
        this.addProperty(PROPERTY_USER_DIRECTORY, userDirectory);
    }

    public void setUserHome(String userHome) {
        this.addProperty(PROPERTY_USER_HOME, userHome);
    }

    public void setUserLanguage(String userLanguage) {
        this.addProperty(PROPERTY_USER_LANGUAGE, userLanguage);
    }

    public void setUserName(String userName) {
        this.addProperty(PROPERTY_USER_NAME, userName);
    }

    public void setUserTimezone(String userTimezone) {
        this.addProperty(PROPERTY_USER_TIMEZONE, userTimezone);
    }

    @Override
    protected Optional<String> getType() {
        return this.eventType;
    }

    private String normalizeName(String name) {
        return name.toLowerCase().replace(' ', '-');
    }
}
