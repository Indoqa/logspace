/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.event;

import java.util.List;

public class EventFilterElementBuilder {

    private String property;
    private String value;
    private Object to;
    private Object from;
    private List<String> values;

    public EventFilterElement build() {
        if (this.value != null) {
            return EqualsEventFilterElement.create(this.property, this.value);
        }

        if (this.from != null || this.to != null) {
            return RangeEventFilterElement.create(this.property, this.from, this.to);
        }

        if (this.values != null) {
            return MultiValueEventFilterElement.create(this.property, this.values);
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

    public void withValues(List<String> values) {
        this.values = values;
    }
}
