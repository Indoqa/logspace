/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.event;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import io.logspace.agent.api.event.Event;
import io.logspace.hq.rest.api.event.EventFilter;
import io.logspace.hq.rest.api.event.EventPage;
import io.logspace.hq.rest.api.suggestion.AgentDescription;
import io.logspace.hq.rest.api.suggestion.Suggestion;
import io.logspace.hq.rest.api.suggestion.SuggestionInput;
import io.logspace.hq.rest.api.timeseries.TimeSeriesDefinition;

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
     * Retrieve stored data for the given {@link TimeSeriesDefinition}
     *
     * @param dataDefinition - The {@link TimeSeriesDefinition} defining the query.
     * @return The matching {@link Event} properties for the {@link TimeSeriesDefinition}
     */
    Object[] getData(TimeSeriesDefinition dataDefinition);

    /**
     * Calculates the {@link Suggestion} of stored information for the given {@link SuggestionInput}.<br>
     * This includes spaces, systems, {@link AgentDescription}s, propertyNames.
     *
     * @param input - The {@link SuggestionInput} to calculate suggestions for.
     * @return The calculated {@link Suggestion}.
     */
    Suggestion getSuggestion(SuggestionInput input);

    /**
     * Retrieves stored {@link Event Events} matching the given filter.
     *
     * This method uses a cursor based approach which allows to efficiently retrieve the Event following a provided "cursor mark". The
     * "cursor mark" needed for moving to the next page is always provided in the response. Paging can only move from one page to next,
     * not to a previous page.
     *
     * The Events will always be sorted chronologically.
     *
     * @param eventFilter The {@link EventFilter} describing which Events to retrieve.
     * @param count The maximum number of Events to retrieve.
     * @param cursorMark The position from which to begin retrieving Events. '*' means from the beginning.
     *
     * @return A {@link EventPage Page of Events}
     */
    EventPage retrieve(EventFilter eventFilter, int count, String cursorMark);

    /**
     * Store the supplied events in the space.
     *
     * @param events - {@link Event}s to store.
     * @param space - Space to store in.
     */
    void store(Collection<? extends Event> events, String space);

    void stream(EventFilter eventFilter, int count, int offset, EventStreamer eventStreamer);

    interface EventStreamer {

        void streamEvent(Event event) throws IOException;

    }
}
