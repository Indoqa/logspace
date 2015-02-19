package io.logspace.agent.api;

import io.logspace.agent.api.event.Event;

import java.util.Collection;

public interface AgentController {

    void register(Agent agent);

    void send(Collection<Event> events);

    void send(Event event);

}
