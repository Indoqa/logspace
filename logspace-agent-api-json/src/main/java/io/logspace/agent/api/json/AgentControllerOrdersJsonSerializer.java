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
import io.logspace.agent.api.order.AgentControllerOrder;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public final class AgentControllerOrdersJsonSerializer {

    private static final String FIELD_COMMIT_MAX_COUNT = "commit-max-count";
    private static final String FIELD_COMMIT_MAX_SECONDS = "commit-max-seconds";
    private static final String FIELD_AGENT_ORDERS = "agent-orders";
    private static final String FIELD_ID = "id";
    private static final String FIELD_TRIGGER_TYPE = "trigger-type";
    private static final String FIELD_TRIGGER_PARAMETER = "trigger-parameter";

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private AgentControllerOrdersJsonSerializer() {
        // hide utility class constructor
    }

    public static AgentControllerOrder fromJson(InputStream inputStream) throws IOException {
        AgentControllerOrder result = new AgentControllerOrder();

        JsonParser parser = JSON_FACTORY.createParser(inputStream);

        validateTokenType(parser.nextToken(), START_OBJECT);

        result.setCommitMaxCount(getIntFieldValue(parser, FIELD_COMMIT_MAX_COUNT));
        result.setCommitMaxSeconds(getIntFieldValue(parser, FIELD_COMMIT_MAX_SECONDS));

        JsonToken token = parser.nextToken();
        if (token == FIELD_NAME) {
            List<AgentOrder> agentOrders = new ArrayList<AgentOrder>();
            result.setAgentOrders(agentOrders);

            validateFieldName(parser.getCurrentName(), FIELD_AGENT_ORDERS);
            validateTokenType(parser.nextToken(), START_ARRAY);

            while (true) {
                token = parser.nextToken();
                if (token == END_ARRAY) {
                    break;
                }

                validateTokenType(token, START_OBJECT);
                agentOrders.add(readAgentOrder(parser));
                validateTokenType(parser.nextToken(), END_OBJECT);
            }
        }

        validateTokenType(parser.nextToken(), END_OBJECT);
        validateTokenType(parser.nextToken(), null);

        return result;
    }

    private static String getFieldValue(JsonParser parser, String fieldName) throws IOException {
        validateTokenType(parser.nextToken(), FIELD_NAME);
        validateFieldName(parser.getCurrentName(), fieldName);

        return parser.nextTextValue();
    }

    private static int getIntFieldValue(JsonParser parser, String fieldName) throws IOException {
        validateTokenType(parser.nextToken(), FIELD_NAME);
        validateFieldName(parser.getCurrentName(), fieldName);

        return parser.nextIntValue(0);
    }

    private static AgentOrder readAgentOrder(JsonParser parser) throws IOException {
        AgentOrder result = new AgentOrder();

        result.setId(getFieldValue(parser, FIELD_ID));
        result.setTriggerType(TriggerType.valueOf(getFieldValue(parser, FIELD_TRIGGER_TYPE)));
        result.setTriggerParameter(getFieldValue(parser, FIELD_TRIGGER_PARAMETER));

        return result;
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