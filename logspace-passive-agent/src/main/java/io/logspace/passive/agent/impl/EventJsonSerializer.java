package io.logspace.passive.agent.impl;

import io.logspace.passive.agent.api.Event;

import java.util.Collection;

public final class EventJsonSerializer {

    private EventJsonSerializer() {
        // hide utility class constructor
    }

    public static String toJson(Collection<Event> event) {
        return "";
    }
}
