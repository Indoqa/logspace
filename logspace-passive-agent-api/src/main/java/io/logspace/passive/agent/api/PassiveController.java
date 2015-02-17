package io.logspace.passive.agent.api;

import java.util.Collection;

public interface PassiveController {

    void send(Collection<Event> events);

    void send(Event event);

}
