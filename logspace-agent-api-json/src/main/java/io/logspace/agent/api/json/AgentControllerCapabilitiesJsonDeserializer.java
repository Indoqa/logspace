/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonToken.END_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static io.logspace.agent.api.order.AgentControllerCapabilities.*;
import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.order.PropertyDescription;
import io.logspace.agent.api.order.PropertyDescription.PropertyUnit;
import io.logspace.agent.api.order.PropertyType;
import io.logspace.agent.api.order.TriggerType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class AgentControllerCapabilitiesJsonDeserializer extends AbstractJsonDeserializer {

    private AgentControllerCapabilitiesJsonDeserializer(byte[] data) throws IOException {
        super();

        this.setData(data);
    }

    private AgentControllerCapabilitiesJsonDeserializer(InputStream inputStream) throws IOException {
        super();

        this.setInputStream(inputStream);
    }

    public static AgentControllerCapabilities fromJson(byte[] data) throws IOException {
        return new AgentControllerCapabilitiesJsonDeserializer(data).deserialize();
    }

    public static AgentControllerCapabilities fromJson(InputStream inputStream) throws IOException {
        return new AgentControllerCapabilitiesJsonDeserializer(inputStream).deserialize();
    }

    private AgentControllerCapabilities deserialize() throws IOException {
        AgentControllerCapabilities result = new AgentControllerCapabilities();

        this.prepareToken();
        this.validateToken(START_OBJECT);
        this.consumeToken();

        result.setId(this.readMandatoryField(FIELD_ID));
        result.setSystem(this.readMandatoryField(FIELD_SYSTEM));
        result.setSpace(this.readOptionalField(FIELD_SPACE));

        this.prepareToken();
        if (this.hasToken(FIELD_NAME)) {
            this.validateField(FIELD_AGENT_CAPABILITIES);

            this.prepareToken();
            this.validateToken(START_ARRAY);
            this.consumeToken();

            while (true) {
                this.prepareToken();
                if (this.hasToken(END_ARRAY)) {
                    this.consumeToken();
                    break;
                }

                this.validateToken(START_OBJECT);
                this.consumeToken();

                result.add(this.readAgentCapabilities());

                this.prepareToken();
                this.validateToken(END_OBJECT);
                this.consumeToken();
            }
        }

        this.prepareToken();
        this.validateToken(END_OBJECT);
        this.consumeToken();

        this.prepareToken();
        this.validateEnd();

        return result;
    }

    private AgentCapabilities readAgentCapabilities() throws IOException {
        AgentCapabilities result = new AgentCapabilities();

        result.setId(this.readMandatoryField(FIELD_ID));
        result.setType(this.readMandatoryField(FIELD_TYPE));

        this.prepareToken();
        this.validateToken(FIELD_NAME);
        this.validateField(FIELD_TRIGGER_TYPES);

        this.prepareToken();
        this.validateToken(START_ARRAY);
        this.consumeToken();

        List<TriggerType> triggerTypes = new ArrayList<TriggerType>();
        while (true) {
            this.prepareToken();
            if (this.hasToken(END_ARRAY)) {
                this.consumeToken();
                break;
            }

            triggerTypes.add(TriggerType.valueOf(this.readString()));
            this.consumeToken();
        }
        result.setSupportedTriggerTypes(triggerTypes.toArray(new TriggerType[triggerTypes.size()]));

        this.prepareToken();
        this.validateToken(FIELD_NAME);
        this.validateField(FIELD_PROPERTY_DESCRIPTIONS);

        this.prepareToken();
        this.validateToken(START_ARRAY);
        this.consumeToken();

        List<PropertyDescription> propertyDescriptions = new ArrayList<PropertyDescription>();
        while (true) {
            this.prepareToken();
            if (this.hasToken(END_ARRAY)) {
                this.consumeToken();
                break;
            }

            this.validateToken(START_OBJECT);
            this.consumeToken();

            propertyDescriptions.add(this.readPropertyDescription());

            this.prepareToken();
            this.validateToken(END_OBJECT);
            this.consumeToken();
        }
        result.setPropertyDescriptions(propertyDescriptions.toArray(new PropertyDescription[propertyDescriptions.size()]));

        return result;
    }

    private PropertyDescription readPropertyDescription() throws IOException {
        PropertyDescription result = new PropertyDescription();

        result.setName(this.readMandatoryField(FIELD_PROPERTY_NAME));
        result.setPropertyType(PropertyType.valueOf(this.readMandatoryField(FIELD_PROPERTY_TYPE)));

        this.prepareToken();
        if (this.hasToken(FIELD_NAME)) {
            this.validateToken(FIELD_NAME);
            this.validateField(FIELD_PROPERTY_UNITS);

            this.prepareToken();
            this.validateToken(START_OBJECT);
            this.consumeToken();

            List<PropertyUnit> propertyUnits = new ArrayList<PropertyUnit>();
            while (true) {
                this.prepareToken();
                if (this.hasToken(END_OBJECT)) {
                    this.consumeToken();
                    break;
                }

                propertyUnits.add(this.readPropertyUnit());

                this.consumeToken();
            }
            result.setUnits(propertyUnits.toArray(new PropertyUnit[propertyUnits.size()]));
        }

        return result;
    }

    private PropertyUnit readPropertyUnit() throws IOException {
        PropertyUnit result = new PropertyUnit();

        result.setName(this.getCurrentName());
        result.setFactor(this.nextDoubleValue());

        return result;
    }
}