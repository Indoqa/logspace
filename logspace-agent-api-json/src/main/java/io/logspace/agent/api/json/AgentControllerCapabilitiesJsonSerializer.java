/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;
import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.order.TriggerType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public final class AgentControllerCapabilitiesJsonSerializer {

    private static final String FIELD_ID = "id";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_TRIGGER_TYPES = "trigger-types";

    private static final String FIELD_AGENT_CAPABILITIES = "agent-capabilities";

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private AgentControllerCapabilitiesJsonSerializer() {
        // hide utility class constructor
    }

    public static String toJson(AgentControllerCapabilities capabilities) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        toJson(capabilities, baos);

        return baos.toString(UTF8.getJavaName());
    }

    public static void toJson(AgentControllerCapabilities capabilities, OutputStream outputStream) throws IOException {
        JsonGenerator generator = JSON_FACTORY.createGenerator(outputStream);

        generator.writeStartObject();

        generator.writeStringField(FIELD_ID, capabilities.getId());

        if (capabilities.getAgentCapabilitiesCount() > 0) {
            generator.writeArrayFieldStart(FIELD_AGENT_CAPABILITIES);

            for (AgentCapabilities eachAgentCapabilities : capabilities.getAgentCapabilities()) {
                generator.writeStartObject();

                generator.writeStringField(FIELD_ID, eachAgentCapabilities.getId());
                generator.writeStringField(FIELD_TYPE, eachAgentCapabilities.getType());

                generator.writeArrayFieldStart(FIELD_TRIGGER_TYPES);
                for (TriggerType eachTriggerType : eachAgentCapabilities.getSupportedTriggerTypes()) {
                    generator.writeString(eachTriggerType.name());
                }
                generator.writeEndArray();

                generator.writeEndObject();
            }

            generator.writeEndArray();
        }

        generator.writeEndObject();

        generator.close();
    }
}