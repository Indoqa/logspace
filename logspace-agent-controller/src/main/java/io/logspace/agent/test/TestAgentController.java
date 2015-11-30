/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import io.logspace.agent.api.AgentController;
import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.api.AgentControllerDescription.Parameter;
import io.logspace.agent.api.AgentControllerException;
import io.logspace.agent.api.AgentControllerProvider;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.agent.api.json.EventJsonSerializer;
import io.logspace.agent.impl.AbstractAgentController;

public class TestAgentController extends AbstractAgentController {

    public static final String OUTPUT_FILE_PARAMETER = "output-file";
    public static final String DEFAULT_OUTPUT_FILE = "./logspace-events.json";

    private final List<Event> collectedEvents = new LinkedList<Event>();
    private final OutputStream outputStream;

    public TestAgentController(AgentControllerDescription agentControllerDescription) {
        super(agentControllerDescription);

        String outputFile = resolveProperties(
            agentControllerDescription.getParameterValue(OUTPUT_FILE_PARAMETER, DEFAULT_OUTPUT_FILE));
        try {
            this.outputStream = new FileOutputStream(outputFile);
        } catch (IOException ioex) {
            throw new AgentControllerException("Could not open output stream to '" + outputFile, ioex);
        }
    }

    public static Event getCollectedEvent(int index) {
        return getCollectedEvents().get(index);
    }

    public static int getCollectedEventCount() {
        return getCollectedEvents().size();
    }

    public static List<Event> getCollectedEvents() {
        AgentController agentController = AgentControllerProvider.getAgentController();
        if (agentController instanceof TestAgentController) {
            return ((TestAgentController) agentController).collectedEvents;
        }

        throw new AgentControllerException(
            "Cannot retrieve collected Events because the current AgentController is not a TestAgentController.");
    }

    public static String getProperty(Collection<? extends EventProperty<String>> eventProperties, String name) {
        for (EventProperty<String> eachEventProperty : eventProperties) {
            if (eachEventProperty.getKey().equals(name)) {
                return eachEventProperty.getValue();
            }
        }

        return null;
    }

    public static void installIfRequired(String outputFile) {
        if (AgentControllerProvider.isInitialized()) {
            return;
        }

        AgentControllerDescription description = new AgentControllerDescription();
        description.setClassName(TestAgentController.class.getName());
        description.setParameters(Arrays.asList(Parameter.create(OUTPUT_FILE_PARAMETER, outputFile)));
        AgentControllerProvider.setDescription(description);
    }

    @Override
    public void flush() {
        try {
            EventJsonSerializer.toJson(this.collectedEvents, this.outputStream);
            this.collectedEvents.clear();
        } catch (IOException ioex) {
            throw new AgentControllerException("Could not write collected events.", ioex);
        }
    }

    @Override
    public void send(Collection<Event> events) {
        this.collectedEvents.addAll(events);
    }
}
