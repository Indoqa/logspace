package io.logspace.agent.impl;

import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.api.AgentControllerDescription.Parameter;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.EventJsonSerializer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TestAgentController extends AbstractAgentController {

    public static final String OUTPUT_FILE_PARAMETER = "output-file";
    public static final String DEFAULT_OUTPUT_FILE = "./logspace-events.json";

    private final List<Event> collectedEvents = new LinkedList<Event>();
    private final OutputStream outputStream;

    public TestAgentController(AgentControllerDescription agentControllerDescription) {
        super();

        String outputFile = agentControllerDescription.getParameterValue(OUTPUT_FILE_PARAMETER, DEFAULT_OUTPUT_FILE);
        try {
            this.outputStream = new FileOutputStream(outputFile);
        } catch (IOException ioex) {
            throw new AgentControllerException("Could not open output stream to '" + outputFile, ioex);
        }
    }

    public static void installIfRequired(String outputFile) {
        if (AgentControllerProvider.isInitialized()) {
            return;
        }

        AgentControllerDescription description = AgentControllerDescription.withClass(TestAgentController.class);
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

    @Override
    public void shutdown() {
        this.flush();
    }
}