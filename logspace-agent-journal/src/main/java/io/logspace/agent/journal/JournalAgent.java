/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.journal;

import io.logspace.agent.api.AbstractApplicationAgent;
import io.logspace.agent.api.event.Event;

public final class JournalAgent extends AbstractApplicationAgent {

    public static final String TYPE = "journal";

    private JournalAgent(String id) {
        super(id, TYPE);
    }

    public static JournalAgent create(String id) {
        return new JournalAgent(id);
    }

    public String triggerEvent(String category, String message) {
        return this.triggerEvent(category, message, null);
    }

    public String triggerEvent(String category, String message, String parentEventId) {
        JournalEventBuilder eventBuilder = JournalEventBuilder.createJournalBuilder(this.getEventBuilderData());

        eventBuilder.setParentEventId(parentEventId);
        eventBuilder.setCategory(category);
        eventBuilder.setMessage(message);

        Event event = eventBuilder.toEvent();
        this.sendEvent(event);
        return event.getId();
    }
}
