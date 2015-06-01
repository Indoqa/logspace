/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.console;

import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.agent.api.util.ConsoleWriter;
import io.logspace.agent.impl.AbstractAgentController;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class ConsoleAgentController extends AbstractAgentController {

    private static final String MESSAGE_PATTERN_PARAMETER_NAME = "message-pattern";
    private static final String DEFAULT_MESSAGE_PATTERN = "{id} (gid:{global-id}, pid:{parent-id}) - {agent-id} [{type}] - {timestamp}: {properties}";

    private final String messagePattern;

    public ConsoleAgentController(AgentControllerDescription agentControllerDescription) {
        super();

        this.messagePattern = agentControllerDescription.getParameterValue(MESSAGE_PATTERN_PARAMETER_NAME, DEFAULT_MESSAGE_PATTERN);
    }

    @Override
    public final void send(Collection<Event> events) {
        for (Event eachEvent : events) {
            this.writeEvent(this.formatEvent(eachEvent));
        }
    }

    protected void writeEvent(String formattedEvent) {
        ConsoleWriter.write(formattedEvent);
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
    }

    private String formatEvent(Event event) {
        StringBuilder stringBuilder = new StringBuilder(this.messagePattern);

        this.replace(stringBuilder, "{id}", event.getId());
        this.replace(stringBuilder, "{global-id}", event.getGlobalEventId().orElse("none"));
        this.replace(stringBuilder, "{parent-id}", event.getParentEventId().orElse("none"));
        this.replace(stringBuilder, "{agent-id}", event.getAgentId());
        this.replace(stringBuilder, "{type}", event.getType().orElse(null));
        this.replace(stringBuilder, "{timestamp}", this.formatDate(event.getTimestamp()));
        this.replace(stringBuilder, "{properties}", this.formatProperties(event));

        return stringBuilder.toString();
    }

    private String formatProperties(Event event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");

        this.writeProperties(stringBuilder, event.getBooleanProperties());
        this.writeProperties(stringBuilder, event.getDateProperties());
        this.writeProperties(stringBuilder, event.getDoubleProperties());
        this.writeProperties(stringBuilder, event.getFloatProperties());
        this.writeProperties(stringBuilder, event.getIntegerProperties());
        this.writeProperties(stringBuilder, event.getLongProperties());
        this.writeProperties(stringBuilder, event.getStringProperties());

        if (stringBuilder.length() > 1) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }

        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private void replace(StringBuilder stringBuilder, String parameter, Object value) {
        while (true) {
            int start = stringBuilder.indexOf(parameter);
            if (start == -1) {
                break;
            }

            int end = start + parameter.length();
            stringBuilder.replace(start, end, String.valueOf(value));
        }
    }

    private void writeProperties(StringBuilder stringBuilder, Collection<? extends EventProperty<?>> properties) {
        for (EventProperty<?> eachProperty : properties) {
            this.writeProperty(stringBuilder, eachProperty);
            stringBuilder.append(", ");
        }
    }

    private void writeProperty(StringBuilder stringBuilder, EventProperty<?> property) {
        stringBuilder.append(property.getKey());
        stringBuilder.append('=');

        if (property.getValue() instanceof String) {
            stringBuilder.append('"');
            stringBuilder.append(property.getValue());
            stringBuilder.append('"');
        } else {
            stringBuilder.append(property.getValue());
        }
    }
}
