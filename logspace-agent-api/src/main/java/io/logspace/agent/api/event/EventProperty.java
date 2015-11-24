/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

/**
 * The interface defining a property of an {@link Event}.
 *
 * @param <T> The type of the value.
 */
public interface EventProperty<T> {

    String getKey();

    T getValue();

}
