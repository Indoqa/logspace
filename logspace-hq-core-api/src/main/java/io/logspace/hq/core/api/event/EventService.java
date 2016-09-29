/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.event;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.EventPage;
import io.logspace.hq.rest.api.event.EventFilter;
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
     * Persist a collection of events into a particular space.
     * 
     * @param events The {@link Event}s to be stored.
     * @param space The target space.
     */
    void store(Collection<? extends Event> events, String space);
}
