/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.journal;

import io.logspace.agent.api.AbstractAgent;
import io.logspace.agent.api.order.TriggerType;

public final class JournalAgent extends AbstractAgent {

    public static final String TYPE = "journal";

    private JournalAgent(String id) {
        super(id, TYPE, TriggerType.Event);
    }

    public static JournalAgent create(String id) {
        return new JournalAgent(id);
    }

    public void triggerEvent(String category, String message) {
        JournalEventBuilder eventBuilder = JournalEventBuilder.createJournalBuilder(this.getId(), this.getSystem());

        eventBuilder.setCategory(category);
        eventBuilder.setMessage(message);

        this.sendEvent(eventBuilder.toEvent());
    }
}
