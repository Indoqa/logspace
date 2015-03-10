package io.logspace.hq.core.api;

import io.logspace.agent.api.event.Event;

import java.util.Collection;

public interface EventService {

    void store(Collection<Event> events);

}
