/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.it;

public class LogspaceTestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LogspaceTestException(String message) {
        super(message);
    }

    public LogspaceTestException(String message, Throwable cause) {
        super(message, cause);
    }
}