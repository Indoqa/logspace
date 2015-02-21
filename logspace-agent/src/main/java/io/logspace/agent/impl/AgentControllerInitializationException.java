package io.logspace.agent.impl;

public class AgentControllerInitializationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AgentControllerInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentControllerInitializationException(Throwable cause) {
        super(cause);
    }
}
