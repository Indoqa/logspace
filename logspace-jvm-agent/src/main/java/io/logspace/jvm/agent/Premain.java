/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.jvm.agent;

import java.lang.instrument.Instrumentation;

public class Premain {

    protected static JvmAgent AGENT;

    public static void agentmain(String args, Instrumentation inst) {
        AGENT = new JvmAgent();
    }

    public static void premain(String args, Instrumentation inst) {
        AGENT = new JvmAgent();
    }
}
