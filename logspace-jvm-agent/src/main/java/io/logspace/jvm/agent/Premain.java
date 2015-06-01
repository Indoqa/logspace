/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.jvm.agent;

import java.lang.instrument.Instrumentation;

public final class Premain {

    private static JvmAgent agent;

    private Premain() {
        // hide utility class constructor
    }

    @SuppressWarnings("unused")
    public static void agentmain(String args, Instrumentation inst) {
        agent = JvmAgent.create();

        agent.sendAgentAttachedEvent();

        registerShutdownHook();
    }

    @SuppressWarnings("unused")
    public static void premain(String args, Instrumentation inst) {
        agent = JvmAgent.create();

        agent.sendJvmStartedEvent();

        registerShutdownHook();
    }

    protected static JvmAgent getAgent() {
        return agent;
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
            Premain.getAgent().sendShutdownEvent();
        }
    }
}
