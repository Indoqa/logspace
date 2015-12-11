/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.order;

/**
 * Defines the types of aggregation that can be applied to the values of an {@link io.logspace.agent.api.event.EventProperty
 * EventProperty}.
 */
public enum Aggregate {
    /**
     * Select the maximum of the values, according to their natural order.
     */
    max,

    /**
     * Select the minimum of the values, according to their natural order.
     */
    min,

    /**
     * Calculate the arithmetic mean of the values, i.e. the <code>sum</code> / <code>count</code>.
     */
    avg,

    /**
     * Select the number of values.
     */
    count,

    /**
     * Calculate the sum of the values.
     */
    sum;
}
