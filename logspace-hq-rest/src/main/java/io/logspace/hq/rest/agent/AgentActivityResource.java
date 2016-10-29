/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.agent;

import static java.util.concurrent.TimeUnit.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import io.logspace.hq.core.api.agent.AgentService;
import io.logspace.hq.rest.AbstractLogspaceResourcesBase;
import io.logspace.hq.rest.api.agentactivity.AgentActivities;
import spark.Request;

@Named
public class AgentActivityResource extends AbstractLogspaceResourcesBase {

    private static final int DEFAULT_START = 0;
    private static final int MIN_START = 0;
    private static final int MAX_START = Integer.MAX_VALUE;

    private static final int DEFAULT_COUNT = 25;
    private static final int MIN_COUNT = 1;
    private static final int MAX_COUNT = 1000;

    private static final int DEFAULT_DURATION = 900;
    private static final int MIN_DURATION = (int) MINUTES.toSeconds(1);
    private static final int MAX_DURATION = (int) DAYS.toSeconds(30);

    private static final int DEFAULT_STEPS = 50;
    private static final int MIN_STEPS = 1;
    private static final int MAX_STEPS = 1000;

    private static final String DEFAULT_SORT = "count desc";

    @Inject
    private AgentService agentService;

    @PostConstruct
    public void mount() {
        this.get("/agent-activity", (req, res) -> this.getAgentActivities(req));
    }

    private AgentActivities getAgentActivities(Request req) {
        int start = getQueryParam(req, "start", DEFAULT_START, MIN_START, MAX_START);
        int count = getQueryParam(req, "count", DEFAULT_COUNT, MIN_COUNT, MAX_COUNT);
        int duration = getQueryParam(req, "duration", DEFAULT_DURATION, MIN_DURATION, MAX_DURATION);
        int steps = getQueryParam(req, "steps", DEFAULT_STEPS, MIN_STEPS, Math.min(duration, MAX_STEPS));
        String sort = getQueryParam(req, "sort", DEFAULT_SORT);

        return this.agentService.getAgentActivities(start, count, duration, steps, sort);
    }
}
