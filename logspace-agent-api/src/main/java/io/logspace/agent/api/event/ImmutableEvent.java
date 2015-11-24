/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * Immutable {@link Event} implementation.
 */
public final class ImmutableEvent implements Event {

    /**
     * The unique identifier of this Event.
     */
    private final String id;

    /**
     * The ID of the Agent which produced this Event.
     */
    private final String agentId;

    /**
     * The Event's creation time.
     */
    private final Date timestamp;

    /**
     * The optional type of this Event, indicating what properties are to be expected.
     */
    private final String type;

    /**
     * The system this Event was created on.
     */
    private final String system;

    /**
     * The optional marker.
     */
    private final String marker;

    /**
     * An optional global Event is the root for multiple sub Events.
     */
    private final String globalEventId;

    /**
     * The optional direct predecessor Event related to this Event.
     */
    private final String parentEventId;

    /**
     * The properties of this Event.
     */
    private final EventProperties properties;

    /**
     * Create an new Event: the <code>id</code> and the <code>timestamp</code> are set automatically by using {@link UUID#randomUUID()}
     * and <code>new {@link Date}</code>.
     *
     * @param agentId The id of the agent recording this Event.
     * @param system The system recording this Event.
     * @param type The Event type.
     * @param globalEventId The global Event id.
     * @param parentEventId The parent Event id.
     * @param marker The marker.
     * @param properties The properties.
     */
    public ImmutableEvent(String agentId, String system, String type, String globalEventId, String parentEventId, String marker,
            EventProperties properties) {
        this.id = UUID.randomUUID().toString();
        this.agentId = agentId;
        this.system = system;
        this.timestamp = new Date();

        this.type = type;
        this.globalEventId = globalEventId;
        this.parentEventId = parentEventId;
        this.marker = marker;

        if (properties == null) {
            this.properties = new EventProperties();
        } else {
            this.properties = properties;
        }
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
     * @see io.logspace.agent.api.event.Event#getAgentId()
     */
    @Override
    public String getAgentId() {
        return this.agentId;
    }

    /**
     * @see io.logspace.agent.api.event.Event#getBooleanProperties()
     */
    @Override
    public Collection<BooleanEventProperty> getBooleanProperties() {
        return this.properties.getBooleanProperties();
    }

    /**
     * @see io.logspace.agent.api.event.Event#getDateProperties()
     */
    @Override
    public Collection<DateEventProperty> getDateProperties() {
        return this.properties.getDateProperties();
    }

    /**
     * @see io.logspace.agent.api.event.Event#getDoubleProperties()
     */
    @Override
    public Collection<DoubleEventProperty> getDoubleProperties() {
        return this.properties.getDoubleProperties();
    }

    /**
     * @see io.logspace.agent.api.event.Event#getFloatProperties()
     */
    @Override
    public Collection<FloatEventProperty> getFloatProperties() {
        return this.properties.getFloatProperties();
    }

    /**
     * @see io.logspace.agent.api.event.Event#getGlobalEventId()
     */
    @Override
    public String getGlobalEventId() {
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
     * @see io.logspace.agent.api.event.Event#getIntegerProperties()
     */
    @Override
    public Collection<IntegerEventProperty> getIntegerProperties() {
        return this.properties.getIntegerProperties();
    }

    /**
     * @see io.logspace.agent.api.event.Event#getLongProperties()
     */
    @Override
    public Collection<LongEventProperty> getLongProperties() {
        return this.properties.getLongProperties();
    }

    /**
     * @see io.logspace.agent.api.event.Event#getMarker()
     */
    @Override
    public String getMarker() {
        return this.marker;
    }

    /**
     * @see io.logspace.agent.api.event.Event#getParentEventId()
     */
    @Override
    public String getParentEventId() {
        return this.parentEventId;
    }

    /**
     * @see io.logspace.agent.api.event.Event#getStringProperties()
     */
    @Override
    public Collection<StringEventProperty> getStringProperties() {
        return this.properties.getStringProperties();
    }

    /**
     * @see io.logspace.agent.api.event.Event#getSystem()
     */
    @Override
    public String getSystem() {
        return this.system;
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
    public String getType() {
        return this.type;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    /**
     * @see io.logspace.agent.api.event.Event#hasProperties()
     */
    @Override
    public boolean hasProperties() {
        return !this.properties.isEmpty();
    }
}
