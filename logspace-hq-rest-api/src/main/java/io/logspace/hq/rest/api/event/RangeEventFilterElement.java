/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.event;

public class RangeEventFilterElement extends AbstractEventFilterElement {

    private Object from;
    private Object to;

    public static EventFilterElement create(String property, Object from, Object to) {
        RangeEventFilterElement result = new RangeEventFilterElement();

        result.setProperty(property);
        result.setFrom(from);
        result.setTo(to);

        return result;
    }

    public Object getFrom() {
        return this.from;
    }

    public Object getTo() {
        return this.to;
    }

    public void setFrom(Object from) {
        this.from = from;
    }

    public void setTo(Object to) {
        this.to = to;
    }
}
