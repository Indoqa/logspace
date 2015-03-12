/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * Immutable {@link Event} implementation.
 */
public final class ImmutableEvent implements Event {

    /**
     * A unique identifier of the event.
     */
    private final String id;

    /**
     * The event's creation time.
     */
    private final Date timestamp;

    /**
     * The optional type of the event indicating what properties are expected.
     */
    private final Optional<String> type;

    /**
     * A optional global event is the root for multiple sub events.
     */
    private Optional<String> globalEventId;

    /**
     * The optional direct predecessor event related to this event.
     */
    private Optional<String> parentEventId;

    /**
     * Key-value pairs of additional information.
     */
    private final Collection<EventProperty> properties;

    /**
     * Create an new event: the <code>id</code> and the <code>timestamp</code> are set automatically by using
     * {@link UUID#randomUUID()} and <code>new {@link Date}</code>.
     * 
     * @param type The event type.
     * @param globalEventId The global event id.
     * @param parentEventId The parent event id.
     * @param properties The properties.
     */
    public ImmutableEvent(Optional<String> type, Optional<String> globalEventId, Optional<String> parentEventId,
            EventProperty... properties) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = new Date();

        this.type = type;
        this.globalEventId = globalEventId;
        this.parentEventId = parentEventId;
        this.properties = Arrays.asList(properties);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        ImmutableEvent other = (ImmutableEvent) obj;
        return this.id.equals(other.id);
    }

    /**
     * @see io.logspace.agent.api.event.Event#getGlobalEventId()
     */
    @Override
    public Optional<String> getGlobalEventId() {
        return this.globalEventId;
    }

    /**
     * @see io.logspace.agent.api.event.Event#getId()
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * @see io.logspace.agent.api.event.Event#getParentEventId()
     */
    @Override
    public Optional<String> getParentEventId() {
        return this.parentEventId;
    }

    /**
     * @see io.logspace.agent.api.event.Event#getProperties()
     */
    @Override
    public Collection<EventProperty> getProperties() {
        return new ArrayList<EventProperty>(this.properties);
    }

    /**
     * @see io.logspace.agent.api.event.Event#getTimestamp()
     */
    @Override
    public Date getTimestamp() {
        return this.timestamp;
    }

    /**
     * @see io.logspace.agent.api.event.Event#getType()
     */
    @Override
    public Optional<String> getType() {
        return this.type;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
