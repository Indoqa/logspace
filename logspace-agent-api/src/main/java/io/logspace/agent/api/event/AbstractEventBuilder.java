/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

import io.logspace.agent.api.event.context.EventContext;

import java.util.Date;

/**
 * Base class for event builders. Extend it to simplify property handling by providing explicit methods.
 */
public abstract class AbstractEventBuilder {

    private final String agentId;
    private final String system;
    private Optional<String> globalEventId;
    private Optional<String> parentEventId;

    private final EventProperties properties = new EventProperties();

    /**
     * Create an event builder with empty {@link #globalEventId} and empty {@link #parentEventId}.
     */
    protected AbstractEventBuilder(String agentId, String system) {
        this(agentId, system, Optional.<String> empty(), Optional.<String> empty());
    }

    /**
     * Create an event builder that uses another event as starting point. It uses the others event <code>id</code> as
     * the new event's {@link #parentEventId} and copies its {@link #globalEventId}.
     *
     * @param parentEvent The parent event to be used as template.
     * @return A new event object.
     */
    protected AbstractEventBuilder(String agentId, String system, Event parentEvent) {
        this(agentId, system, Optional.of(parentEvent.getId()), parentEvent.getGlobalEventId());
    }

    protected AbstractEventBuilder(String agentId, String system, Optional<String> parentEventId, Optional<String> globalEventId) {
        this.agentId = agentId;
        this.system = system;

        this.parentEventId = parentEventId;
        this.globalEventId = globalEventId;
    }

    public AbstractEventBuilder setEventContext(EventContext eventContext) {
        if (eventContext == null) {
            return this;
        }

        if (eventContext.hasGlobalId()) {
            this.setGlobalEventId(eventContext.getGlobalId());
        }

        if (eventContext.hasParentId()) {
            this.setParentEventId(eventContext.getParentId());
        }

        return this;
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
        return new ImmutableEvent(this.agentId, this.system, this.getType(), this.globalEventId, this.parentEventId, this.properties);
    }

    protected final void addProperty(BooleanEventProperty property) {
        this.properties.add(property);
    }

    protected final void addProperty(DateEventProperty property) {
        this.properties.add(property);
    }

    protected final void addProperty(DoubleEventProperty property) {
        this.properties.add(property);
    }

    protected final void addProperty(FloatEventProperty property) {
        this.properties.add(property);
    }

    protected final void addProperty(IntegerEventProperty property) {
        this.properties.add(property);
    }

    protected final void addProperty(LongEventProperty property) {
        this.properties.add(property);
    }

    protected final void addProperty(String key, Boolean value) {
        this.properties.add(new BooleanEventProperty(key, value));
    }

    protected final void addProperty(String key, Date value) {
        this.properties.add(new DateEventProperty(key, value));
    }

    protected final void addProperty(String key, Double value) {
        this.addProperty(new DoubleEventProperty(key, value));
    }

    protected final void addProperty(String key, Float value) {
        this.properties.add(new FloatEventProperty(key, value));
    }

    protected final void addProperty(String key, Integer value) {
        this.properties.add(new IntegerEventProperty(key, value));
    }

    protected final void addProperty(String key, Long value) {
        this.properties.add(new LongEventProperty(key, value));
    }

    protected final void addProperty(String key, String value) {
        this.addProperty(new StringEventProperty(key, value));
    }

    /**
     * A subclass uses this method to add event properties.
     *
     * @param property The additional property.
     */
    protected final void addProperty(StringEventProperty property) {
        this.properties.add(property);
    }

    /**
     * @return The optional type of the event.
     */
    protected abstract Optional<String> getType();

}
