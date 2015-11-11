/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonToken.*;
import static io.logspace.agent.api.order.AgentControllerOrder.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.logspace.agent.api.order.AgentControllerOrder;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;

public final class AgentControllerOrdersJsonDeserializer extends AbstractJsonDeserializer {

    private AgentControllerOrdersJsonDeserializer(InputStream inputStream) throws IOException {
        super();

        this.setInputStream(inputStream);
    }

    public static AgentControllerOrder fromJson(InputStream inputStream) throws IOException {
        return new AgentControllerOrdersJsonDeserializer(inputStream).deserialize();
    }

    private AgentControllerOrder deserialize() throws IOException {
        AgentControllerOrder result = new AgentControllerOrder();

        this.prepareToken();
        this.validateTokenType(START_OBJECT);
        this.consumeToken();

        this.prepareToken();
        this.validateTokenType(FIELD_NAME);

        if (this.hasField(FIELD_AGENT_ORDERS)) {
            this.advance();

            List<AgentOrder> agentOrders = new ArrayList<AgentOrder>();
            result.setAgentOrders(agentOrders);

            this.prepareToken();
            this.validateTokenType(START_ARRAY);
            this.consumeToken();

            while (true) {
                this.prepareToken();

                if (this.hasToken(END_ARRAY)) {
                    this.consumeToken();
                    break;
                }

                this.validateTokenType(START_OBJECT);
                this.consumeToken();

                agentOrders.add(this.readAgentOrder());

                this.prepareToken();
                this.validateTokenType(END_OBJECT);
                this.consumeToken();
            }
        }

        result.setCommitMaxCount(this.readOptionalIntField(FIELD_COMMIT_MAX_COUNT));
        result.setCommitMaxSeconds(this.readOptionalIntField(FIELD_COMMIT_MAX_SECONDS));

        this.prepareToken();
        this.validateTokenType(END_OBJECT);
        this.consumeToken();

        this.prepareToken();
        this.validateEnd();

        return result;
    }

    private AgentOrder readAgentOrder() throws IOException {
        AgentOrder result = new AgentOrder();

        result.setId(this.readMandatoryField(FIELD_ID));
        result.setTriggerType(TriggerType.get(this.readMandatoryField(FIELD_TRIGGER_TYPE)));

        this.prepareToken();
        if (this.hasToken(FIELD_NAME)) {
            result.setTriggerParameter(this.readOptionalField(FIELD_TRIGGER_PARAMETER));
        } else {
            result.setTriggerParameter(null);
        }

        return result;
    }
}
