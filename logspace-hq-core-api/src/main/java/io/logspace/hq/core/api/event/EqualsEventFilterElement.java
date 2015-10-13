/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.event;

public class EqualsEventFilterElement extends AbstractEventFilterElement {

    private Object value;

    public static EventFilterElement create(String property, String value) {
        EqualsEventFilterElement result = new EqualsEventFilterElement();

        result.setPropertyName(property);
        result.setValue(value);

        return result;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
