/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for event builders. Extend it to simplify property handling by providing explicit methods.
 */
public abstract class AbstractEventBuilder {

    private Optional<String> globalEventId;
    private Optional<String> parentEventId;

    private final Map<String, EventProperty> properties = new HashMap<String, EventProperty>();

    /**
     * Create an event builder with empty {@link #globalEventId} and empty {@link #parentEventId}.
     */
    public AbstractEventBuilder() {
        super();
        this.parentEventId = Optional.empty();
        this.globalEventId = Optional.empty();
    }

    /**
     * Create an event builder that uses another event as starting point. It uses the others event <code>id</code> as
     * the new event's {@link #parentEventId} and copies its {@link #globalEventId}.
     * 
     * @param parentEvent The parent event to be used as template.
     * @return A new event object.
     */
    public AbstractEventBuilder(final Event parentEvent) {
        this.parentEventId = Optional.of(parentEvent.getId());
        this.globalEventId = parentEvent.getGlobalEventId();
    }

    /**
     * Set the optional global event id.
     * 
     * @param globalEventId The optional global event id.
     * @return This event build object.
     */
    public AbstractEventBuilder setGlobalEventId(String globalEventId) {
        this.globalEventId = Optional.of(globalEventId);
        return this;
    }

    /**
     * Set the optional parent event id.
     * 
     * @param parentEventId The optional parent event id.
     * @return This event builder object.
     */
    public AbstractEventBuilder setParentEventId(String parentEventId) {
        this.parentEventId = Optional.of(parentEventId);
        return this;
    }

    public final Event toEvent() {
        EventProperty[] properties = this.properties.values().toArray(new EventProperty[this.properties.size()]);
        return new ImmutableEvent(this.getType(), this.globalEventId, this.parentEventId, properties);
    }

    /**
     * A subclass uses this method to add event properties.
     * 
     * @param property The additional property.
     */
    protected final void add(EventProperty property) {
        this.properties.put(property.getKey(), property);
    }

    /**
     * @return The optional type of the event.
     */
    protected abstract Optional<String> getType();
}
