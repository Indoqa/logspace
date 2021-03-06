/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.logspace.agent.api.event.Event;

public class EventPage implements Iterable<Event> {

    private List<Event> events = new ArrayList<Event>();

    private String nextCursorMark;

    private long totalCount;

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public List<Event> getEvents() {
        return this.events;
    }

    public String getNextCursorMark() {
        return this.nextCursorMark;
    }

    public long getTotalCount() {
        return this.totalCount;
    }

    @Override
    public Iterator<Event> iterator() {
        return this.events.iterator();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setNextCursorMark(String nextCursorMark) {
        this.nextCursorMark = nextCursorMark;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
