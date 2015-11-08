/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static io.logspace.agent.api.AgentControllerDescription.FIELD_CLASS_NAME;
import static io.logspace.agent.api.AgentControllerDescription.FIELD_ID;
import static io.logspace.agent.api.AgentControllerDescription.FIELD_PARAMETERS;
import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.api.AgentControllerDescription.Parameter;
import io.logspace.agent.api.AgentControllerDescriptionDeserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class AgentControllerDescriptionJsonDeserializer extends AbstractJsonDeserializer
        implements AgentControllerDescriptionDeserializer {

    public static AgentControllerDescription fromJson(byte[] data) throws IOException {
        AgentControllerDescriptionJsonDeserializer deserializer = new AgentControllerDescriptionJsonDeserializer();
        return deserializer.fromJson(new ByteArrayInputStream(data));
    }

    @Override
    public AgentControllerDescription fromJson(InputStream inputStream) throws IOException {
        this.setInputStream(inputStream);
        return this.deserialize();
    }

    private AgentControllerDescription deserialize() throws IOException {
        AgentControllerDescription result = new AgentControllerDescription();

        this.prepareToken();
        this.validateTokenType(START_OBJECT);
        this.consumeToken();

        result.setId(this.readMandatoryField(FIELD_ID));
        result.setClassName(this.readMandatoryField(FIELD_CLASS_NAME));

        this.prepareToken();
        if (this.hasToken(FIELD_NAME)) {
            List<Parameter> parameters = new ArrayList<Parameter>();
            result.setParameters(parameters);

            this.validateFieldName(FIELD_PARAMETERS);

            this.prepareToken();
            this.validateTokenType(START_OBJECT);
            this.consumeToken();

            while (true) {
                this.prepareToken();

                this.validateTokenType(END_OBJECT, FIELD_NAME);

                if (this.hasToken(END_OBJECT)) {
                    this.consumeToken();
                    break;
                }

                parameters.add(this.readParameter());
            }
        }

        this.prepareToken();
        this.validateTokenType(END_OBJECT);
        this.consumeToken();

        this.prepareToken();
        this.validateEnd();

        return result;
    }

    private Parameter readParameter() throws IOException {
        Parameter parameter = new Parameter();

        parameter.setName(this.getCurrentName());
        parameter.setValue(this.nextTextValue());

        this.consumeToken();

        return parameter;
    }
}
