/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.impl;

import io.logspace.agent.api.Agent;
import io.logspace.agent.api.AgentController;
import io.logspace.agent.api.event.Event;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractAgentController implements AgentController {

    private final Set<Agent> agents = Collections.synchronizedSet(new HashSet<Agent>());

    @Override
    public void flush() {
        // default does nothing
    }

    @Override
    public void intialize() {
        // default does nothing
    }

    @Override
    public final void register(Agent agent) {
        this.agents.add(agent);
    }

    @Override
    public final void send(Event event) {
        this.send(Collections.singleton(event));
    }

    @Override
    public void shutdown() {
        // default does nothing
    }

    @Override
    public final void unregister(Agent agent) {
        this.agents.remove(agent);
    }
}