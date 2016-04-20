/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.event;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.EventPage;
import io.logspace.hq.rest.api.agentactivity.AgentActivities;
import io.logspace.hq.rest.api.event.EventFilter;
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
     * Delete stored {@link Event Events} with the given ids.
     * 
     * @param ids A list of the event ids.
     */
    void delete(List<String> ids);

    /**
     * Execute a query directly against the underlying Event Store using the given parameters.
     *
     * @param parameters The parameters to be used for the query.
     *
     * @return A {@link NativeQueryResult} with the result.
     */
    NativeQueryResult executeNativeQuery(Map<String, String[]> parameters);

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

    /**
     * Retrieve stored data for the given {@link TimeSeriesDefinition}
     *
     * @param dataDefinition - The {@link TimeSeriesDefinition} defining the query.
     * @return The matching {@link Event} properties for the {@link TimeSeriesDefinition}
     */
    Object[] getTimeSeries(TimeSeriesDefinition dataDefinition);

    /**
     * Retrieves stored {@link Event Events} matching the given filter.
     *
     * This method uses a cursor based approach which allows to efficiently retrieve the Event following a provided "cursor mark". The
     * "cursor mark" needed for moving to the next page is always provided in the response. Paging can only move from one page to next,
     * not to a previous page.
     *
     * The Events are sorted by ascending chronological order.
     *
     * @param eventFilter The {@link EventFilter} describing which Events to retrieve.
     * @param count The maximum number of Events to retrieve.
     * @param cursorMark The position from which to begin retrieving Events. '*' means from the beginning.
     *
     * @return A {@link EventPage} Page of Events sorted by ascending chronological order.
     */
    EventPage retrieve(EventFilter eventFilter, int count, String cursorMark);

    /**
     * The same as {@link EventService#retrieve(EventFilter, int, String)} but sorted by descending chronological order.
     *
     * @param eventFilter The {@link EventFilter} describing which Events to retrieve.
     * @param count The maximum number of Events to retrieve.
     * @param cursorMark The position from which to begin retrieving Events. '*' means from the beginning.
     *
     * @return A {@link EventPage} Page of Events sorted by descending chronological order.
     */
    EventPage retrieveReversed(EventFilter eventFilter, int count, String cursorMark);

    /**
     * Store the supplied events in the space.
     *
     * @param events - {@link Event}s to store.
     * @param space - Space to store in.
     */
    void store(Collection<? extends Event> events, String space);

    void stream(EventFilter eventFilter, int count, int offset, EventStreamer eventStreamer);

    void stream(TimeSeriesDefinition timeSeriesDefinition, EventStreamer eventStreamer);

    interface EventStreamer {

        void streamEvent(Event event) throws IOException;

    }
}
