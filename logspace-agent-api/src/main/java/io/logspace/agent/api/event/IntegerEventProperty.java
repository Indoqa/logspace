/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

/**
 * An {@link EventProperty} carrying a {@link Integer} as value.
 */
public class IntegerEventProperty extends AbstractEventProperty<Integer> {

    public IntegerEventProperty(String key, Integer value) {
        super(key, value);
    }
}
