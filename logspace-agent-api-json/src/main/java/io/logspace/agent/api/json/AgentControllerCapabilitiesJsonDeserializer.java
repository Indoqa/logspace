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
import static io.logspace.agent.api.order.AgentControllerCapabilities.FIELD_AGENT_CAPABILITIES;
import static io.logspace.agent.api.order.AgentControllerCapabilities.FIELD_ID;
import static io.logspace.agent.api.order.AgentControllerCapabilities.FIELD_TRIGGER_TYPES;
import static io.logspace.agent.api.order.AgentControllerCapabilities.FIELD_TYPE;
import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.order.TriggerType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class AgentControllerCapabilitiesJsonDeserializer extends AbstractJsonDeserializer {

    private AgentControllerCapabilitiesJsonDeserializer(byte[] data) throws IOException {
        super(data);
    }

    public static AgentControllerCapabilities fromJson(byte[] data) throws IOException {
        return new AgentControllerCapabilitiesJsonDeserializer(data).deserialize();
    }

    private AgentControllerCapabilities deserialize() throws IOException {
        AgentControllerCapabilities result = new AgentControllerCapabilities();

        this.prepareToken();
        this.validateToken(START_OBJECT);
        this.consumeToken();

        result.setId(this.readMandatoryField(FIELD_ID));

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
                break;
            }

            triggerTypes.add(TriggerType.valueOf(this.readString()));
            this.consumeToken();
        }
        result.setSupportedTriggerTypes(triggerTypes.toArray(new TriggerType[triggerTypes.size()]));

        this.prepareToken();
        this.validateToken(END_ARRAY);
        this.consumeToken();

        return result;
    }
}