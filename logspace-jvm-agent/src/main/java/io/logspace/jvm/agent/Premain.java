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

    public static void agentmain(String args, Instrumentation inst) {
        agent = JvmAgent.create();
        globalEventId = createGlobalEventId();

        agent.sendAgentAttachedEvent(globalEventId);

        registerShutdownHook();
    }

    public static void premain(String args, Instrumentation inst) {
        agent = JvmAgent.create();
        globalEventId = createGlobalEventId();

        agent.sendJvmStartEvent(globalEventId);

        registerShutdownHook();
    }

    protected static JvmAgent getAgent() {
        return agent;
    }

    protected static String getGlobalEventId() {
        return globalEventId;
    }

    private static String createGlobalEventId() {
        return UUID.randomUUID().toString();
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
            Premain.getAgent().sendJvmStopEvent(Premain.getGlobalEventId());
        }
    }
}
