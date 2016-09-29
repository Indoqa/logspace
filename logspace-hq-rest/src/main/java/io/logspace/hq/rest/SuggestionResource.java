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

import io.logspace.hq.core.api.agent.AgentService;
import io.logspace.hq.rest.api.suggestion.Suggestion;
import io.logspace.hq.rest.api.suggestion.SuggestionInput;
import spark.Request;

@Named
public class SuggestionResource extends AbstractLogspaceResourcesBase {

    @Inject
    private AgentService agentService;

    @PostConstruct
    public void mount() {
        this.get("/suggestion", (req, res) -> this.getSuggestion(req));
        this.post("/suggestion", (req, res) -> this.postSuggestion(req));
    }

    private Suggestion getSuggestion(Request req) {
        SuggestionInput input = new SuggestionInput();
        input.setPropertyId(getQueryParam(req, "property", null));
        input.setSpaceId(getQueryParam(req, "space", null));
        input.setSystemId(getQueryParam(req, "system", null));
        input.setText(getQueryParam(req, "input", null));
        return this.getSuggestion(input);
    }

    private Suggestion getSuggestion(SuggestionInput input) {
        return this.agentService.getSuggestion(input);
    }

    private Suggestion postSuggestion(Request req) {
        SuggestionInput input = this.getTransformer().toObject(req.body(), SuggestionInput.class);
        return this.getSuggestion(input);
    }
}
