/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.jvm.agent;

import io.logspace.agent.api.AbstractAgent;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;

import com.sun.management.UnixOperatingSystemMXBean;

public final class JvmAgent extends AbstractAgent {

    public static final String SYSTEM_PROPERTY_JVM_IDENTIFIER = "io.logspace.jvm-identifier";

    private JvmAgent() {
        super("jvm/" + getJvmIdentifier(), "jvm", TriggerType.Off, TriggerType.Cron);
    }

    public static JvmAgent create() {
        return new JvmAgent();
    }

    private static String getJvmIdentifier() {
        String jvmIdentifier = System.getProperty(SYSTEM_PROPERTY_JVM_IDENTIFIER);

        if (jvmIdentifier == null || jvmIdentifier.isEmpty()) {
            throw new IllegalArgumentException("System Property: '" + SYSTEM_PROPERTY_JVM_IDENTIFIER
                    + "' not defined. Please set this property to a unique value.");
        }

        return jvmIdentifier;
    }

    @Override
    public void execute(AgentOrder agentOrder) {
        if (!this.isEnabled()) {
            return;
        }
        JvmEventBuilder eventBuilder = JvmEventBuilder.createJvmBuilder(this.getId(), this.getSystem());

        this.addOperatingSystemProperties(eventBuilder);
        this.addGarbageCollectorProperties(eventBuilder);
        this.addThreadProperties(eventBuilder);
        this.addMemoryProperties(eventBuilder);
        this.addClassLoadingProperties(eventBuilder);

        this.sendEvent(eventBuilder.toEvent());
    }

    public void sendAgentAttachedEvent() {
        if (!this.isEnabled()) {
            return;
        }

        JvmEventBuilder eventBuilder = JvmEventBuilder.createJvmAgentAttachedBuilder(this.getId(), this.getSystem());

        this.addSystemInformation(eventBuilder);

        this.sendEvent(eventBuilder.toEvent());
    }

    public void sendJvmStartedEvent() {
        if (!this.isEnabled()) {
            return;
        }

        JvmEventBuilder eventBuilder = JvmEventBuilder.createJvmStartBuilder(this.getId(), this.getSystem());

        this.addSystemInformation(eventBuilder);

        this.sendEvent(eventBuilder.toEvent());
    }

    public void sendShutdownEvent() {
        if (!this.isEnabled()) {
            return;
        }

        JvmEventBuilder eventBuilder = JvmEventBuilder.createJvmStopBuilder(this.getId(), this.getSystem());
        this.sendEvent(eventBuilder.toEvent());
    }

    private void addClassLoadingProperties(JvmEventBuilder eventBuilder) {
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();

        eventBuilder.setLoadedClassCount(classLoadingMXBean.getLoadedClassCount());
        eventBuilder.setTotalLoadedClassCount(classLoadingMXBean.getTotalLoadedClassCount());
        eventBuilder.setUnloadedClassCount(classLoadingMXBean.getUnloadedClassCount());
    }

    private void addGarbageCollectorProperties(JvmEventBuilder eventBuilder) {
        for (GarbageCollectorMXBean eachGarbageCollector : ManagementFactory.getGarbageCollectorMXBeans()) {
            long collectionCount = eachGarbageCollector.getCollectionCount();
            if (collectionCount <= 0) {
                continue;
            }
            eventBuilder.setGarbageCollectorRunCount(eachGarbageCollector.getName(), collectionCount);
            eventBuilder.setGarbageCollectorTime(eachGarbageCollector.getName(), eachGarbageCollector.getCollectionTime());
        }
    }

    private void addMemoryProperties(JvmEventBuilder eventBuilder) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        eventBuilder.setInitialHeapMemory(heapMemoryUsage.getInit());
        eventBuilder.setMaxHeapMemory(heapMemoryUsage.getMax());
        eventBuilder.setUsedHeapMemory(heapMemoryUsage.getUsed());
        eventBuilder.setCommitedHeapMemory(heapMemoryUsage.getCommitted());

        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        eventBuilder.setInitialNonHeapMemory(nonHeapMemoryUsage.getInit());
        eventBuilder.setMaxNonHeapMemory(nonHeapMemoryUsage.getMax());
        eventBuilder.setUsedNonHeapMemory(nonHeapMemoryUsage.getUsed());
        eventBuilder.setCommitedNonHeapMemory(nonHeapMemoryUsage.getCommitted());

        eventBuilder.setObjectPendingFinalizationCount(memoryMXBean.getObjectPendingFinalizationCount());
    }

    private void addOperatingSystemProperties(JvmEventBuilder eventBuilder) {
        OperatingSystemMXBean operatingSystemBean = ManagementFactory.getOperatingSystemMXBean();

        if (operatingSystemBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean operatingSystem = (com.sun.management.OperatingSystemMXBean) operatingSystemBean;
            eventBuilder.setProcessCpuLoad(operatingSystem.getProcessCpuLoad());
            eventBuilder.setProcessCpuTime(operatingSystem.getProcessCpuTime());
        }
        if (operatingSystemBean instanceof UnixOperatingSystemMXBean) {
            UnixOperatingSystemMXBean unixOperatingSystem = (UnixOperatingSystemMXBean) operatingSystemBean;
            eventBuilder.setMaxFileDescriptorCount(unixOperatingSystem.getMaxFileDescriptorCount());
            eventBuilder.setOpenFileDescriptorCount(unixOperatingSystem.getOpenFileDescriptorCount());
        }
    }

    private void addSystemInformation(JvmEventBuilder eventBuilder) {
        eventBuilder.setAvailableProcessors(Runtime.getRuntime().availableProcessors());
        eventBuilder.setCpuEndian(System.getProperty("sun.cpu.endian"));

        eventBuilder.setJavaRuntimeName(System.getProperty("java.runtime.name"));
        eventBuilder.setJavaRuntimeVersion(System.getProperty("java.runtime.version"));

        eventBuilder.setJvmVersion(System.getProperty("java.vm.version"));
        eventBuilder.setJvmVendor(System.getProperty("java.vm.vendor"));
        eventBuilder.setJvmName(System.getProperty("java.vm.name"));
        eventBuilder.setJvmInfo(System.getProperty("java.vm.info"));

        eventBuilder.setOsName(System.getProperty("os.name"));
        eventBuilder.setOsArchitecture(System.getProperty("os.arch"));
        eventBuilder.setOsVersion(System.getProperty("os.version"));

        eventBuilder.setUserCountry(System.getProperty("user.country"));
        eventBuilder.setUserDirectory(System.getProperty("user.dir"));
        eventBuilder.setUserHome(System.getProperty("user.home"));
        eventBuilder.setUserLanguage(System.getProperty("user.language"));
        eventBuilder.setUserName(System.getProperty("user.name"));
        eventBuilder.setUserTimezone(System.getProperty("user.timezone"));
    }

    private void addThreadProperties(JvmEventBuilder eventBuilder) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        eventBuilder.setThreadCount(threadMXBean.getThreadCount());
        eventBuilder.setDaemonThreadCount(threadMXBean.getDaemonThreadCount());
    }
}
