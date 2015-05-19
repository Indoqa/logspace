/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import static io.logspace.agent.api.HttpStatusCode.BadRequest;
import io.logspace.agent.api.order.Aggregate;
import io.logspace.agent.api.order.PropertyType;
import io.logspace.hq.core.api.AbstractLogspaceResourceException;

import java.text.MessageFormat;

public final class InvalidDataDefinitionException extends AbstractLogspaceResourceException {

    private static final long serialVersionUID = 1L;

    private InvalidDataDefinitionException(String message) {
        super(message, BadRequest, "INVALID_DATA_DEFINITION");
    }

    public static InvalidDataDefinitionException illegalAggregate(PropertyType propertyType, Aggregate aggregate) {
        InvalidDataDefinitionException result = new InvalidDataDefinitionException(MessageFormat.format(
                "Cannot combine a property of type ''{0}'' with aggregation ''{1}''. Supported aggregates for ''{2}'' are {3}",
                propertyType, aggregate, propertyType, propertyType.getAllowedAggregates()));

        result.setParameter("property-type", propertyType);
        result.setParameter("illegal-aggregate", aggregate);
        result.setParameter("allowed-aggregates", propertyType.getAllowedAggregates());

        return result;
    }
}
