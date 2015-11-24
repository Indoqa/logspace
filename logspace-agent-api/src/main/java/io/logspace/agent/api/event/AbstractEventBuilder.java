/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

import java.util.Date;

/**
 * Base class for Event Builders.<br>
 * Each instance is intended for creating a single Event. <br>
 * <br>
 * Subclasses should provide additional methods for setting specific {@link EventProperty EventProperties}.
 */
public abstract class AbstractEventBuilder {

    private final EventBuilderData eventBuilderData;

    private String globalEventId;

    private String parentEventId;

    private final EventProperties properties = new EventProperties();

    /**
     * Create an event builder with empty {@link #globalEventId} and empty {@link #parentEventId}.
     *
     * @param agentId The id of the agent recording this event.
     *
     * @param system The system recording this event.
     */
    protected AbstractEventBuilder(EventBuilderData eventBuilderData) {
        this.eventBuilderData = eventBuilderData;
    }

    /**
     * Set the optional global event id.
     *
     * @param globalEventId The optional global event id.
     * @return This event build object.
     */
    public AbstractEventBuilder setGlobalEventId(String globalEventId) {
        this.globalEventId = globalEventId;
        return this;
    }

    /**
     * Set the optional parent event id.
     *
     * @param parentEventId The optional parent event id.
     * @return This event builder object.
     */
    public AbstractEventBuilder setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
        return this;
    }

    public final Event toEvent() {
        return new ImmutableEvent(this.eventBuilderData.getAgentId(), this.eventBuilderData.getSystem(), this.getType(),
            this.globalEventId, this.parentEventId, this.eventBuilderData.getMarker(), this.properties);
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
    protected abstract String getType();

}
