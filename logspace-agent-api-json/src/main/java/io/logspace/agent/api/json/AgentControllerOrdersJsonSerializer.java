/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;
import static io.logspace.agent.api.order.AgentControllerOrder.FIELD_AGENT_ORDERS;
import static io.logspace.agent.api.order.AgentControllerOrder.FIELD_COMMIT_MAX_COUNT;
import static io.logspace.agent.api.order.AgentControllerOrder.FIELD_COMMIT_MAX_SECONDS;
import static io.logspace.agent.api.order.AgentControllerOrder.FIELD_ID;
import static io.logspace.agent.api.order.AgentControllerOrder.FIELD_TRIGGER_PARAMETER;
import static io.logspace.agent.api.order.AgentControllerOrder.FIELD_TRIGGER_TYPE;
import io.logspace.agent.api.order.AgentControllerOrder;
import io.logspace.agent.api.order.AgentOrder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class AgentControllerOrdersJsonSerializer extends AbstractJsonSerializer {

    private AgentControllerOrdersJsonSerializer(OutputStream outputStream) throws IOException {
        super(outputStream);
    }

    public static String toJson(AgentControllerOrder order) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        toJson(order, baos);

        return baos.toString(UTF8.getJavaName());
    }

    public static void toJson(AgentControllerOrder order, OutputStream outputStream) throws IOException {
        new AgentControllerOrdersJsonSerializer(outputStream).serialize(order);
    }

    private void serialize(AgentControllerOrder value) throws IOException {
        this.startObject();

        this.writeAgentOrders(value);
        this.writeCommitSettings(value);

        this.endObject();

        this.finish();
    }

    private void writeAgentOrders(AgentControllerOrder value) throws IOException {
        if (value.hasAgentOrders()) {
            this.writeField(FIELD_AGENT_ORDERS);
            this.startArray();

            for (AgentOrder eachAgentOrder : value.getAgentOrders()) {
                this.startObject();

                this.writeMandatoryField(FIELD_ID, eachAgentOrder.getId());
                this.writeMandatoryField(FIELD_TRIGGER_TYPE, String.valueOf(eachAgentOrder.getTriggerType()));
                this.writeOptionalField(FIELD_TRIGGER_PARAMETER, eachAgentOrder.getTriggerParameter());

                this.endObject();
            }

            this.endArray();
        }
    }

    private void writeCommitSettings(AgentControllerOrder value) throws IOException {
        this.writeOptionalIntField(FIELD_COMMIT_MAX_COUNT, value.getCommitMaxCount());
        this.writeOptionalIntField(FIELD_COMMIT_MAX_SECONDS, value.getCommitMaxSeconds());
    }
}
