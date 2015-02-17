package io.logspace.active.agent.api;

/**
 * A passive agent is notified by its controller to send events.
 */
public interface ActiveAgent {

    void receiveEventRequest(EventRequest eventRequest);

}
