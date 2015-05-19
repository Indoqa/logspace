/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.order;

import static io.logspace.agent.api.order.Aggregate.avg;
import static io.logspace.agent.api.order.Aggregate.count;
import static io.logspace.agent.api.order.Aggregate.max;
import static io.logspace.agent.api.order.Aggregate.min;
import static io.logspace.agent.api.order.Aggregate.sum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum PropertyType {

    BOOLEAN(count), DATE(count, min, max, avg), INTEGER(count, min, max, avg, sum), LONG(count, min, max, avg, sum), FLOAT(count, min,
            max, avg, sum), DOUBLE(count, min, max, avg, sum), STRING(count);

    private static final Map<String, PropertyType> PROPERTY_TYPES = new HashMap<String, PropertyType>();

    private final Set<Aggregate> allowedAggregates = new HashSet<Aggregate>();

    static {
        for (PropertyType eachValue : PropertyType.values()) {
            PROPERTY_TYPES.put(eachValue.name().toLowerCase(), eachValue);
        }
    }

    private PropertyType(Aggregate... allowedAggregate) {
        for (Aggregate eachAllowedAggregate : allowedAggregate) {
            this.allowedAggregates.add(eachAllowedAggregate);
        }
    }

    public static PropertyType get(String name) {
        if (name == null) {
            return null;
        }

        return PROPERTY_TYPES.get(name.toLowerCase());
    }

    public Set<Aggregate> getAllowedAggregates() {
        return this.allowedAggregates;
    }

    public boolean isAllowed(Aggregate aggregate) {
        return this.allowedAggregates.contains(aggregate);
    }
}