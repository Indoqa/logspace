/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import io.logspace.hq.core.api.event.EventService;
import io.logspace.hq.rest.api.timeseries.*;
import spark.Request;

@Named
public class TimeSeriesResource extends AbstractLogspaceResourcesBase {

    public static final int MAX_STEPS = 10000;

    @Inject
    private EventService eventService;

    @PostConstruct
    public void mount() {
        this.post("/time-series", (req, res) -> this.postTimeSeries(req));
    }

    private TimeSeries postTimeSeries(Request req) {
        TimeSeries result = new TimeSeries();

        TimeSeriesDefinitions definitions = this.getTransformer().toObject(req.body(), TimeSeriesDefinitions.class);
        this.validateTimeWindows(definitions);

        TimeWindow timeWindow = null;
        Object[][] data = new Object[definitions.getDefinitionCount()][];
        for (int i = 0; i < data.length; i++) {
            TimeSeriesDefinition definition = definitions.getDefinition(i);
            data[i] = this.eventService.getData(definition);
            timeWindow = definition.getTimeWindow();
        }

        result.setTimeWindow(timeWindow);
        result.setData(data);

        return result;
    }

    private void validateTimeWindow(TimeWindow timeWindow) {
        int requestedSteps = timeWindow.getSteps();
        if (requestedSteps > MAX_STEPS) {
            throw InvalidTimeSeriesDefinitionException.tooManyValues(requestedSteps, MAX_STEPS);
        }

        if (!timeWindow.getEnd().after(timeWindow.getStart())) {
            throw InvalidTimeSeriesDefinitionException.invalidRange(timeWindow.getStart(), timeWindow.getEnd());
        }
    }

    private void validateTimeWindows(TimeSeriesDefinitions definitions) {
        for (TimeSeriesDefinition eachDefinition : definitions) {
            this.validateTimeWindow(eachDefinition.getTimeWindow());
        }
    }
}
