/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.jvm.agent;

import java.lang.instrument.Instrumentation;
import java.util.UUID;

public final class Premain {

    private static JvmAgent agent;

    private static String globalEventId;

    private Premain() {
        // hide utility class constructor
    }

    @SuppressWarnings("unused")
    public static void agentmain(String args, Instrumentation inst) {
        initialize();

        sendAttachedEvent();
    }

    @SuppressWarnings("unused")
    public static void premain(String args, Instrumentation inst) {
        initialize();

        sendStartEvent();
    }

    protected static JvmAgent getAgent() {
        return agent;
    }

    protected static String getGlobalEventId() {
        return globalEventId;
    }

    protected static void sendAttachedEvent() {
        agent.sendAgentAttachedEvent(globalEventId);
    }

    protected static void sendShutdownEvent() {
        agent.sendJvmStopEvent(globalEventId);
    }

    protected static void sendStartEvent() {
        agent.sendJvmStartEvent(globalEventId);
    }

    private static String createGlobalEventId() {
        return UUID.randomUUID().toString();
    }

    private static void initialize() {
        initializeAgent();
        initializeGlobalEventId();
        registerShutdownHook();
    }

    private static void initializeAgent() {
        agent = JvmAgent.create();
    }

    private static void initializeGlobalEventId() {
        globalEventId = createGlobalEventId();
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new SendJvmShutdownEventHook());
    }

    private static class SendJvmShutdownEventHook extends Thread {

        public SendJvmShutdownEventHook() {
            super();
        }

        @Override
        public void run() {
            Premain.sendShutdownEvent();
        }
    }
}
