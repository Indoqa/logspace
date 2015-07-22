/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp.resource;

import io.logspace.hq.core.api.DataDefinition;
import io.logspace.hq.core.api.DateRange;
import io.logspace.hq.core.api.EventService;
import io.logspace.hq.core.api.InvalidDataDefinitionException;
import io.logspace.hq.core.api.Suggestion;
import io.logspace.hq.core.api.SuggestionInput;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import spark.Request;
import spark.Response;

import com.indoqa.boot.AbstractJsonResourcesBase;

@Named
public class QueryResource extends AbstractJsonResourcesBase {

    public static final int MAX_STEPS = 10000;

    @Inject
    private EventService eventService;

    @PostConstruct
    public void mount() {
        this.post("/query", (req, res) -> this.postQuery(req, res));
        this.post("/suggest", (req, res) -> this.getSuggestion(req, res));
    }

    @SuppressWarnings("unused")
    private Suggestion getSuggestion(Request req, Response res) {
        SuggestionInput input = this.getTransformer().toObject(req.body(), SuggestionInput.class);

        return this.eventService.getSuggestion(input);
    }

    @SuppressWarnings("unused")
    private DataResponse postQuery(Request req, Response res) {
        DataResponse result = new DataResponse();

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