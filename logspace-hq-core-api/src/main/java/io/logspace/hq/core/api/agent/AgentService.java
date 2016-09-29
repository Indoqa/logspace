/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.agent;

import java.util.Set;

import io.logspace.hq.rest.api.agentactivity.AgentActivities;
import io.logspace.hq.rest.api.suggestion.Suggestion;
import io.logspace.hq.rest.api.suggestion.SuggestionInput;

public interface AgentService {

    AgentActivities getAgentActivities(int start, int count, int durationSeconds, int steps, String sort);

    Set<String> getEventPropertyNames(String... globalAgentIds);

    /**
     * Calculates the {@link Suggestion} of stored information for the given {@link SuggestionInput}.<br>
     * This includes spaces, systems, {@link io.logspace.hq.rest.api.suggestion.AgentDescription AgentDescriptions}, propertyNames.
     *
     * @param input - The {@link SuggestionInput} to calculate suggestions for.
     * @return The calculated {@link Suggestion}.
     */
    Suggestion getSuggestion(SuggestionInput input);
}
