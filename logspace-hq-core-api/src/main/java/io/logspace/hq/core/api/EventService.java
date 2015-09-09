/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import io.logspace.agent.api.event.Event;

/**
 * The responsibilities of the event service are:
 * <ul>
 * <li>store supplied events in the given space</li>
 * <li>calculate suggestions</li>
 * <li>retrieve stored data</li>
 * </ul>
 */
public interface EventService {

    /**
     * Execute a query directly against the underlying Event Store using the given parameters.
     *
     * @param parameters The parameters to be used for the query.
     *
     * @return An {@link InputStream} with the unprocessed response.
     */
    InputStream executeDirectQuery(Map<String, String[]> parameters);

    /**
     * Retrieve stored data for the given {@link DataDefinition}
     *
     * @param dataDefinition - The {@link DataDefinition} defining the query.
     * @return The matching {@link Event} properties for the {@link DataDefinition}
     */
    Object[] getData(DataDefinition dataDefinition);

    /**
     * Calculates the {@link Suggestion} of stored information for the given {@link SuggestionInput}.<br>
     * This includes spaces, systems, {@link AgentDescription}s, propertyNames.
     *
     * @param input - The {@link SuggestionInput} to calculate suggestions for.
     * @return The calculated {@link Suggestion}.
     */
    Suggestion getSuggestion(SuggestionInput input);

    /**
     * Store the supplied events in the space.
     *
     * @param events - {@link Event}s to store.
     * @param space - Space to store in.
     */
    void store(Collection<? extends Event> events, String space);

}
