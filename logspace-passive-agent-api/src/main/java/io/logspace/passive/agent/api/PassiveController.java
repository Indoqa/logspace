package io.logspace.passive.agent.api;

import io.logspace.passive.agent.api.event.Event;

import java.util.Collection;

public interface PassiveController {

    void register(HqOrderReceiver hqOrderReceiver);

    void send(Collection<Event> events);

    void send(Event event);

}
