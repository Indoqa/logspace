/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.event;

import io.logspace.agent.api.event.*;

import java.util.Collection;
import java.util.Date;

public class StoredEvent implements Event {

    private String id;
    private String system;
    private String agentId;
    private Date timestamp;
    private Optional<String> parentEventId;
    private Optional<String> globalEventId;
    private Optional<String> type;
    private EventProperties properties = new EventProperties();

    public void addProperties(String propertyName, Object value) {
        if (value instanceof Collection) {
            for (Object eachValue : (Collection<?>) value) {
                this.addProperty(propertyName, eachValue);
            }
        } else {
            this.addProperty(propertyName, value);
        }
    }

    @Override
    public String getAgentId() {
        return this.agentId;
    }

    @Override
    public Collection<BooleanEventProperty> getBooleanProperties() {
        return this.properties.getBooleanProperties();
    }

    @Override
    public Collection<DateEventProperty> getDateProperties() {
        return this.properties.getDateProperties();
    }

    @Override
    public Collection<DoubleEventProperty> getDoubleProperties() {
        return this.properties.getDoubleProperties();
    }

    @Override
    public Collection<FloatEventProperty> getFloatProperties() {
        return this.properties.getFloatProperties();
    }

    @Override
    public Optional<String> getGlobalEventId() {
        return this.globalEventId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Collection<IntegerEventProperty> getIntegerProperties() {
        return this.properties.getIntegerProperties();
    }

    @Override
    public Collection<LongEventProperty> getLongProperties() {
        return this.properties.getLongProperties();
    }

    @Override
    public Optional<String> getParentEventId() {
        return this.parentEventId;
    }

    @Override
    public Collection<StringEventProperty> getStringProperties() {
        return this.properties.getStringProperties();
    }

    @Override
    public String getSystem() {
        return this.system;
    }

    @Override
    public Date getTimestamp() {
        return this.timestamp;
    }

    @Override
    public Optional<String> getType() {
        return this.type;
    }

    @Override
    public boolean hasProperties() {
        return this.properties != null && !this.properties.isEmpty();
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public void setGlobalEventId(Optional<String> globalEventId) {
        this.globalEventId = globalEventId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setParentEventId(Optional<String> parentEventId) {
        this.parentEventId = parentEventId;
    }

    public void setProperties(EventProperties properties) {
        this.properties = properties;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(Optional<String> type) {
        this.type = type;
    }

    private void addProperty(String propertyName, Object value) {
        if (value instanceof Boolean) {
            this.properties.add(new BooleanEventProperty(propertyName, (Boolean) value));
        }

        if (value instanceof Date) {
            this.properties.add(new DateEventProperty(propertyName, (Date) value));
        }

        if (value instanceof Double) {
            this.properties.add(new DoubleEventProperty(propertyName, (Double) value));
        }

        if (value instanceof Float) {
            this.properties.add(new FloatEventProperty(propertyName, (Float) value));
        }

        if (value instanceof Integer) {
            this.properties.add(new IntegerEventProperty(propertyName, (Integer) value));
        }

        if (value instanceof Long) {
            this.properties.add(new LongEventProperty(propertyName, (Long) value));
        }

        if (value instanceof String) {
            this.properties.add(new StringEventProperty(propertyName, (String) value));
        }
    }
}
