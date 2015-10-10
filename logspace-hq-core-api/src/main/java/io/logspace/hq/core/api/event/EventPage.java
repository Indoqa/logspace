/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.event;

import java.util.ArrayList;
import java.util.List;

import io.logspace.agent.api.event.Event;

public class EventPage {

    private List<Event> events = new ArrayList<>();

    private String nextCursorMark;

    private long count;

    public long getCount() {
        return this.count;
    }

    public List<Event> getEvents() {
        return this.events;
    }

    public String getNextCursorMark() {
        return this.nextCursorMark;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setNextCursorMark(String nextCursorMark) {
        this.nextCursorMark = nextCursorMark;
    }
}
