/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

import java.text.MessageFormat;

/**
 * An immutable key-value pair representation of an event property containing a String.
 */
public class StringEventProperty extends AbstractEventProperty<String> {

    public StringEventProperty(String key, String value) {
        super(key, value);
    }

    @Override
    public String toString() {
        return MessageFormat.format("[String-Property key={0}, value={1}]", this.getKey(), this.getValue());
    }
}
