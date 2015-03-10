package io.logspace.agent.impl;

import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.api.event.Event;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingAgentController extends AbstractAgentController {

    public static final String MESSAGE_PATTERN_PARAMETER_NAME = "message-pattern";

    private static final String DEFAULT_MESSAGE_PATTERN = "{id} ({global-id}, {parent-id}) - [{type}] - {timestamp}: {properties}";
    private static final String[] PARAMETERS = {"{id}", "{global-id}", "{parent-id}", "{type}", "{timestamp}", "{properties}"};

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String messagePattern = DEFAULT_MESSAGE_PATTERN;

    public LoggingAgentController(AgentControllerDescription agentControllerDescription) {
        super();

        this.messagePattern = agentControllerDescription.getParameterValue(MESSAGE_PATTERN_PARAMETER_NAME, DEFAULT_MESSAGE_PATTERN);
    }

    @Override
    public void send(Collection<Event> events) {
        for (Event eachEvent : events) {
            this.logger.error(this.fillInParameters(this.messagePattern, eachEvent));
        }
    }

    private String fillInParameters(String message, Event event) {
        StringBuilder stringBuilder = new StringBuilder(message);

        for (String eachParameter : PARAMETERS) {
            while (true) {
                int start = stringBuilder.indexOf(eachParameter);
                if (start == -1) {
                    break;
                }
                int end = start + eachParameter.length();
                stringBuilder.replace(start, end, event.getId());
            }
        }

        return stringBuilder.toString();
    }
}