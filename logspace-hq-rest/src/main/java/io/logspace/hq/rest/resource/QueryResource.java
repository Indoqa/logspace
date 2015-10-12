/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.resource;

import io.logspace.hq.core.api.event.DataDefinition;
import io.logspace.hq.core.api.event.DateRange;
import io.logspace.hq.core.api.event.EventService;
import io.logspace.hq.core.api.event.NativeQueryParameters;
import io.logspace.hq.core.api.model.InvalidDataDefinitionException;
import io.logspace.hq.core.api.model.Suggestion;
import io.logspace.hq.core.api.model.SuggestionInput;
import io.logspace.hq.rest.model.DataQuery;
import io.logspace.hq.rest.model.TimeSeries;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;

import spark.Request;
import spark.Response;
import spark.Spark;

@Named
public class QueryResource extends AbstractLogspaceResourcesBase {

    public static final int MAX_STEPS = 10000;

    @Inject
    private EventService eventService;

    @PostConstruct
    public void mount() {
        this.post("/time-series", (req, res) -> this.postTimeSeries(req));

        this.get("/suggest", (req, res) -> this.getSuggestion(req));
        this.post("/suggest", (req, res) -> this.postSuggestion(req));

        Spark.get(this.resolvePath("/native-query"), (req, res) -> this.getNativeQuery(req, res));
        Spark.post(this.resolvePath("/native-query"), (req, res) -> this.postNativeQuery(req, res));
    }

    private void executeNativeQuery(Response res, Map<String, String[]> parameters) throws IOException {
        res.type("application/json");

        InputStream inputStream = this.eventService.executeDirectQuery(parameters);
        IOUtils.copy(inputStream, res.raw().getOutputStream());
        inputStream.close();
    }

    private Object getNativeQuery(Request req, Response res) throws IOException {
        this.executeNativeQuery(res, req.raw().getParameterMap());

        return "";
    }

    private Suggestion getSuggestion(Request req) {
        SuggestionInput input = new SuggestionInput();
        input.setPropertyId(getParam(req, "property", null));
        input.setSpaceId(getParam(req, "space", null));
        input.setSystemId(getParam(req, "system", null));
        input.setText(getParam(req, "input", null));

        return this.getSuggestion(input);
    }

    private Suggestion getSuggestion(SuggestionInput input) {
        return this.eventService.getSuggestion(input);
    }

    private Object postNativeQuery(Request req, Response res) throws IOException {
        NativeQueryParameters parameters = this.getTransformer().toObject(req.body(), NativeQueryParameters.class);

        this.executeNativeQuery(res, parameters.getParameters());

        return "";
    }

    private Suggestion postSuggestion(Request req) {
        SuggestionInput input = this.getTransformer().toObject(req.body(), SuggestionInput.class);

        return this.getSuggestion(input);
    }

    private TimeSeries postTimeSeries(Request req) {
        TimeSeries result = new TimeSeries();

        DataQuery dataQuery = this.getTransformer().toObject(req.body(), DataQuery.class);
        this.validateDateRanges(dataQuery);

        DateRange dateRange = null;
        List<DataDefinition> dataDefinitions = dataQuery.getDataDefinitions();
        Object[][] data = new Object[dataDefinitions.size()][];
        for (int i = 0; i < dataDefinitions.size(); i++) {
            DataDefinition dataDefinition = dataDefinitions.get(i);
            data[i] = this.eventService.getData(dataDefinition);
            dateRange = dataDefinition.getDateRange();
        }

        result.setData(data);
        result.setDateRange(dateRange);

        return result;
    }

    private void validateDateRange(DateRange dateRange) {
        int requestedSteps = dateRange.getSteps();
        if (requestedSteps > MAX_STEPS) {
            throw InvalidDataDefinitionException.tooManyValues(requestedSteps, MAX_STEPS);
        }

        if (!dateRange.getEnd().after(dateRange.getStart())) {
            throw InvalidDataDefinitionException.invalidRange(dateRange.getStart(), dateRange.getEnd());
        }
    }

    private void validateDateRanges(DataQuery dataQuery) {
        for (DataDefinition eachDataDefinition : dataQuery) {
            this.validateDateRange(eachDataDefinition.getDateRange());
        }
    }
}
