/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

/**
 * A {@link RuntimeException} that can be thrown by {@link AgentController AgentControllers} or {@link Agent Agents}.
 */
public class AgentControllerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AgentControllerException(String message) {
        super(message);
    }

    public AgentControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentControllerException(Throwable cause) {
        super(cause);
    }
}
