/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.event;

public class EventFilterElementBuilder {

    private String property;
    private String value;
    private Object to;
    private Object from;

    public EventFilterElement build() {
        if (this.value != null) {
            return EqualsEventFilterElement.create(this.property, this.value);
        }

        if (this.from != null || this.to != null) {
            return RangeEventFilterElement.create(this.property, this.from, this.to);
        }

        return null;
    }

    public void withFrom(Object from) {
        this.from = from;
    }

    public void withProperty(String property) {
        this.property = property;
    }

    public void withTo(Object to) {
        this.to = to;
    }

    public void withValue(String value) {
        this.value = value;
    }
}
