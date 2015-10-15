/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.timeseries;

import static io.logspace.agent.api.HttpStatusCode.BadRequest;
import static java.text.MessageFormat.format;
import io.logspace.agent.api.order.Aggregate;
import io.logspace.agent.api.order.PropertyType;
import io.logspace.hq.rest.api.AbstractLogspaceResourceException;

import java.text.MessageFormat;
import java.util.Date;

public final class InvalidTimeSeriesDefinitionException extends AbstractLogspaceResourceException {

    private static final long serialVersionUID = 1L;

    private InvalidTimeSeriesDefinitionException(String message) {
        super(message, BadRequest, "INVALID_DATA_DEFINITION");
    }

    public static InvalidTimeSeriesDefinitionException illegalAggregate(PropertyType propertyType, Aggregate aggregate) {
        InvalidTimeSeriesDefinitionException result = new InvalidTimeSeriesDefinitionException(MessageFormat.format(
            "Cannot combine a property of type ''{0}'' with aggregation ''{1}''. Supported aggregates for ''{2}'' are {3}",
            propertyType, aggregate, propertyType, propertyType.getAllowedAggregates()));

        result.setParameter("property-type", propertyType);
        result.setParameter("illegal-aggregate", aggregate);
        result.setParameter("allowed-aggregates", propertyType.getAllowedAggregates());

        return result;
    }

    public static InvalidTimeSeriesDefinitionException invalidRange(Date start, Date end) {
        InvalidTimeSeriesDefinitionException result = new InvalidTimeSeriesDefinitionException(
            format("The start of the range must be before its end."));

        result.setParameter("start", start);
        result.setParameter("end", end);

        return result;
    }

    public static InvalidTimeSeriesDefinitionException tooManyValues(int requestedValues, int maxValues) {
        InvalidTimeSeriesDefinitionException result = new InvalidTimeSeriesDefinitionException(format(
            "Cannot create a data response with {0} values. The allowed maximum is {1}. Either decrease the range or increase the gap.",
            requestedValues, maxValues));

        result.setParameter("requested-values", requestedValues);
        result.setParameter("max-values", maxValues);

        return result;
    }
}
