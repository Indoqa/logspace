package io.logspace.passive.controller;

import io.logspace.passive.agent.api.Event;

public interface PassiveController {

    void send(Event event);

}
