/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;
import static io.logspace.agent.api.order.AgentControllerCapabilities.*;
import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.order.PropertyDescription;
import io.logspace.agent.api.order.PropertyDescription.PropertyUnit;
import io.logspace.agent.api.order.TriggerType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public final class AgentControllerCapabilitiesJsonSerializer extends AbstractJsonSerializer {

    private AgentControllerCapabilitiesJsonSerializer(OutputStream outputStream) throws IOException {
        super(outputStream);
    }

    public static String toJson(AgentControllerCapabilities capabilities) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        toJson(capabilities, baos);

        return baos.toString(UTF8.getJavaName());
    }

    public static void toJson(AgentControllerCapabilities capabilities, OutputStream outputStream) throws IOException {
        new AgentControllerCapabilitiesJsonSerializer(outputStream).serialize(capabilities);
    }

    private void serialize(AgentControllerCapabilities capabilities) throws IOException {
        this.startObject();

        this.writeAttributes(capabilities);
        this.writeAgentCapabilities(capabilities.getAgentCapabilities());

        this.endObject();

        this.finish();
    }

    private void writeAgentCapabilities(AgentCapabilities agentCapabilities) throws IOException {
        this.writeMandatoryField(FIELD_ID, agentCapabilities.getId());
        this.writeMandatoryField(FIELD_TYPE, agentCapabilities.getType());

        this.writeField(FIELD_TRIGGER_TYPES);
        this.startArray();
        for (TriggerType eachTriggerType : agentCapabilities.getSupportedTriggerTypes()) {
            this.writeString(eachTriggerType.name());
        }
        this.endArray();

        this.writeField(FIELD_PROPERTY_DESCRIPTIONS);
        this.startArray();
        if (agentCapabilities.hasPropertyDescriptions()) {
            for (PropertyDescription eachPropertyDescription : agentCapabilities.getPropertyDescriptions()) {
                this.startObject();
                this.writePropertyDescription(eachPropertyDescription);
                this.endObject();
            }
        }
        this.endArray();
    }

    private void writeAgentCapabilities(List<AgentCapabilities> agentCapabilities) throws IOException {
        if (agentCapabilities == null || agentCapabilities.isEmpty()) {
            return;
        }

        this.writeField(FIELD_AGENT_CAPABILITIES);
        this.startArray();
        for (AgentCapabilities eachAgentCapabilities : agentCapabilities) {
            this.startObject();
            this.writeAgentCapabilities(eachAgentCapabilities);
            this.endObject();
        }
        this.endArray();
    }

    private void writeAttributes(AgentControllerCapabilities capabilities) throws IOException {
        this.writeMandatoryField(FIELD_ID, capabilities.getId());
        this.writeMandatoryField(FIELD_SYSTEM, capabilities.getSystem());
        this.writeOptionalField(FIELD_SPACE, capabilities.getSpace());
    }

    private void writePropertyDescription(PropertyDescription propertyDescription) throws IOException {
        this.writeMandatoryField(FIELD_PROPERTY_NAME, propertyDescription.getName());
        this.writeMandatoryField(FIELD_PROPERTY_TYPE, propertyDescription.getPropertyType().name());

        if (propertyDescription.hasUnits()) {
            this.writeField(FIELD_PROPERTY_UNITS);

            this.startObject();

            for (PropertyUnit eachPropertyUnit : propertyDescription.getUnits()) {
                this.writeMandatoryDoubleField(eachPropertyUnit.getName(), eachPropertyUnit.getFactor());
            }

            this.endObject();
        }
    }
}