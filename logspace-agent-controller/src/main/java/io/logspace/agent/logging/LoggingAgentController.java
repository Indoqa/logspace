/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.logging;

import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.console.ConsoleAgentController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingAgentController extends ConsoleAgentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public LoggingAgentController(AgentControllerDescription agentControllerDescription) {
        super(agentControllerDescription);
    }

    @Override
    protected void writeEvent(String formattedEvent) {
        this.logger.info(formattedEvent);
    }
}
