/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.journal;

import io.logspace.agent.api.event.AbstractEventBuilder;
import io.logspace.agent.api.event.EventBuilderData;

public final class JournalEventBuilder extends AbstractEventBuilder {

    private static final String PROPERTY_CATEGORY = "category";
    private static final String PROPERTY_MESSAGE = "message";

    private static final String JOURNAL_EVENT_TYPE = "journal";

    private String eventType;

    private JournalEventBuilder(EventBuilderData eventBuilderData, String eventType) {
        super(eventBuilderData);

        this.eventType = eventType;
    }

    public static JournalEventBuilder createJournalBuilder(EventBuilderData eventBuilderData) {
        return new JournalEventBuilder(eventBuilderData, JOURNAL_EVENT_TYPE);
    }

    public JournalEventBuilder setCategory(String category) {
        this.addProperty(PROPERTY_CATEGORY, category);
        return this;
    }

    public JournalEventBuilder setMessage(String message) {
        this.addProperty(PROPERTY_MESSAGE, message);
        return this;
    }

    /**
     * @see io.logspace.agent.api.event.AbstractEventBuilder#getType()
     */
    @Override
    protected String getType() {
        return this.eventType;
    }
}
