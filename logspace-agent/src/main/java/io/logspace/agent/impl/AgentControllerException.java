package io.logspace.agent.impl;

public class AgentControllerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AgentControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentControllerException(Throwable cause) {
        super(cause);
    }
}
