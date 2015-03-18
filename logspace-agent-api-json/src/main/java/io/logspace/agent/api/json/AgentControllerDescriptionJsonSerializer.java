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
import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.api.AgentControllerDescription.Parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public final class AgentControllerDescriptionJsonSerializer {

    private static final String FIELD_CLASS_NAME = "class-name";
    private static final String FIELD_PARAMETERS = "parameters";
    private static final String FIELD_PARAMETER_NAME = "parameter-name";
    private static final String FIELD_PARAMETER_VALUE = "parameter-value";

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private AgentControllerDescriptionJsonSerializer() {
        // hide utility class constructor
    }

    public static AgentControllerDescription fromJson(InputStream inputStream) throws IOException {
        AgentControllerDescription result = new AgentControllerDescription();

        JsonParser parser = JSON_FACTORY.createParser(inputStream);

        validateTokenType(parser.nextToken(), START_OBJECT);

        result.setClassName(getFieldValue(parser, FIELD_CLASS_NAME));

        JsonToken token = parser.nextToken();
        if (token == FIELD_NAME) {
            List<Parameter> parameters = new ArrayList<Parameter>();
            result.setParameters(parameters);

            validateTokenType(token, FIELD_NAME);
            validateFieldName(parser.getCurrentName(), FIELD_PARAMETERS);

            validateTokenType(parser.nextToken(), START_ARRAY);

            while (true) {
                token = parser.nextToken();
                if (token == END_ARRAY) {
                    break;
                }

                validateTokenType(token, START_OBJECT);
                parameters.add(readParameter(parser));
                validateTokenType(parser.nextToken(), END_OBJECT);
            }
        }

        validateTokenType(parser.nextToken(), END_OBJECT);
        validateTokenType(parser.nextToken(), null);

        return result;
    }

    public static void toJson(AgentControllerDescription description, OutputStream outputStream) throws IOException {
        JsonGenerator generator = JSON_FACTORY.createGenerator(outputStream);

        generator.writeStartObject();

        generator.writeStringField(FIELD_CLASS_NAME, description.getClassName());

        if (description.getParameterCount() > 0) {
            generator.writeFieldName(FIELD_PARAMETERS);

            generator.writeStartArray(description.getParameterCount());

            for (Parameter eachParameter : description.getParameters()) {
                generator.writeStartObject();
                generator.writeStringField(FIELD_PARAMETER_NAME, eachParameter.getName());
                generator.writeStringField(FIELD_PARAMETER_VALUE, eachParameter.getValue());
                generator.writeEndObject();
            }

            generator.writeEndArray();
        }

        generator.writeEndObject();

        generator.close();
    }

    private static String getFieldValue(JsonParser parser, String fieldName) throws IOException {
        validateTokenType(parser.nextToken(), FIELD_NAME);
        validateFieldName(parser.getCurrentName(), fieldName);

        return parser.nextTextValue();
    }

    private static Parameter readParameter(JsonParser parser) throws IOException {
        Parameter parameter = new Parameter();

        parameter.setName(getFieldValue(parser, FIELD_PARAMETER_NAME));
        parameter.setValue(getFieldValue(parser, FIELD_PARAMETER_VALUE));

        return parameter;
    }

    private static void validateFieldName(String fieldName, String expectedFieldName) {
        if (!fieldName.equals(expectedFieldName)) {
            throw new IllegalArgumentException("Expected field of name '" + expectedFieldName + "', but found field of name '"
                    + fieldName + "'.");
        }
    }

    private static void validateTokenType(JsonToken token, JsonToken expected) {
        if (token != expected) {
            throw new IllegalArgumentException("Expected token of type " + expected + ", but found " + token);
        }
    }
}