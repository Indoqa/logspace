/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

import java.util.ArrayList;
import java.util.Collection;

public class EventProperties {

    private final Collection<BooleanEventProperty> booleanProperties = new ArrayList<BooleanEventProperty>();
    private final Collection<DateEventProperty> dateProperties = new ArrayList<DateEventProperty>();
    private final Collection<IntegerEventProperty> integerProperties = new ArrayList<IntegerEventProperty>();
    private final Collection<LongEventProperty> longProperties = new ArrayList<LongEventProperty>();
    private final Collection<FloatEventProperty> floatProperties = new ArrayList<FloatEventProperty>();
    private final Collection<DoubleEventProperty> doubleProperties = new ArrayList<DoubleEventProperty>();
    private final Collection<StringEventProperty> stringProperties = new ArrayList<StringEventProperty>();

    public void add(BooleanEventProperty property) {
        this.booleanProperties.add(property);
    }

    public void add(DateEventProperty e) {
        this.dateProperties.add(e);
    }

    public void add(DoubleEventProperty e) {
        this.doubleProperties.add(e);
    }

    public void add(FloatEventProperty e) {
        this.floatProperties.add(e);
    }

    public void add(IntegerEventProperty e) {
        this.integerProperties.add(e);
    }

    public void add(LongEventProperty e) {
        this.longProperties.add(e);
    }

    public void add(StringEventProperty e) {
        this.stringProperties.add(e);
    }

    public Collection<BooleanEventProperty> getBooleanProperties() {
        return this.booleanProperties;
    }

    public Collection<DateEventProperty> getDateProperties() {
        return this.dateProperties;
    }

    public Collection<DoubleEventProperty> getDoubleProperties() {
        return this.doubleProperties;
    }

    public Collection<FloatEventProperty> getFloatProperties() {
        return this.floatProperties;
    }

    public Collection<IntegerEventProperty> getIntegerProperties() {
        return this.integerProperties;
    }

    public Collection<LongEventProperty> getLongProperties() {
        return this.longProperties;
    }

    public Collection<StringEventProperty> getStringProperties() {
        return this.stringProperties;
    }

    public boolean isEmpty() {
        return false;
    }
}
