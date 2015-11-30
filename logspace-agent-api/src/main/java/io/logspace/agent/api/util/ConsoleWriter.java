/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.util;

import java.io.PrintStream;

/**
 * A utility class for writing message directly to the standard output stream.<br>
 * <br>
 * Writing <b>system<b> messages can be disabled by having a system-property with the name 'logspace.suppress-system-message'.
 */
public final class ConsoleWriter {

    private static final String PROPERTY_SUPPRESS_SYSTEM = "logspace.suppress-system-message";

    private static final PrintStream OUT = System.out;

    private ConsoleWriter() {
        // hide utility class constructor
    }

    public static void write(String message) {
        OUT.println(message);
    }

    public static void writeSystem(String message) {
        if (System.getProperties().containsKey(PROPERTY_SUPPRESS_SYSTEM)) {
            return;
        }

        write(message);
    }
}
