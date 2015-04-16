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
import io.logspace.hq.core.api.Suggestion;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import spark.Request;
import spark.Response;

import com.indoqa.boot.AbstractJsonResourcesBase;

@Named
public class QueryResource extends AbstractJsonResourcesBase {

    private static final String PARAMETER_INPUT = "input";

    @Inject
    private EventService eventService;

    @PostConstruct
    public void mount() {
        this.post("/query", (req, res) -> this.postQuery(req, res));
        this.get("/suggest/:" + PARAMETER_INPUT, (req, res) -> this.getSuggestion(req, res));
    }

    @SuppressWarnings("unused")
    private Suggestion getSuggestion(Request req, Response res) {
        String input = req.params(PARAMETER_INPUT);

        return this.eventService.getSuggestion(input);
    }

    @SuppressWarnings("unused")
    private DataResponse postQuery(Request req, Response res) {
        DataResponse result = new DataResponse();

        DataQuery dataQuery = this.getTransformer().toObject(req.body(), DataQuery.class);

        DateRange dateRange = null;
        List<DataDefinition> dataDefinitions = dataQuery.getDataDefinitions();
        Object[][] data = new Object[dataDefinitions.size()][];
        for (int i = 0; i < dataDefinitions.size(); i++) {
            DataDefinition dataDefinition = dataDefinitions.get(i);
            System.out.println(dataDefinition.getAggregate() + " of " + dataDefinition.getPropertyId() + " from "
                    + dataDefinition.getAgentId() + " in " + dataDefinition.getSpace());
            dateRange = dataDefinition.getDateRange();
            data[i] = this.eventService.getData(dataDefinition);
        }

        result.setData(data);
        result.setDateRange(dateRange);

        return result;
    }
}