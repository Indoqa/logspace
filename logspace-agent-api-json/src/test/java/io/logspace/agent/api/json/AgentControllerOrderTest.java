/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static org.junit.Assert.assertEquals;
import io.logspace.agent.api.event.Optional;
import io.logspace.agent.api.order.AgentControllerOrder;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.Test;

public class AgentControllerOrderTest {

    private static final Random RANDOM = new Random();

    @Test
    public void test() throws IOException {
        for (int i = 0; i < 100; i++) {
            AgentControllerOrder expected = new AgentControllerOrder();
            expected.setCommitMaxCount(Optional.of(RANDOM.nextInt(Integer.MAX_VALUE)));
            expected.setCommitMaxSeconds(Optional.of(RANDOM.nextInt(Integer.MAX_VALUE)));
            expected.setAgentOrders(this.getRandomAgentOrders());

            String json = AgentControllerOrdersJsonSerializer.toJson(expected);
            AgentControllerOrder actual = AgentControllerOrdersJsonDeserializer
                .fromJson(new ByteArrayInputStream(json.getBytes("UTF-8")));

            this.compare(expected, actual);
        }
    }

    private void compare(AgentControllerOrder expected, AgentControllerOrder actual) {
        assertEquals(expected.getCommitMaxCount(), actual.getCommitMaxCount());
        assertEquals(expected.getCommitMaxSeconds(), actual.getCommitMaxSeconds());

        for (int i = 0; i < expected.getAgentOrdersCount(); i++) {
            this.compare(expected.getAgentOrders().get(i), actual.getAgentOrders().get(i));
        }
    }

    private void compare(AgentOrder expected, AgentOrder actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTriggerParameter(), actual.getTriggerParameter());
        assertEquals(expected.getTriggerType(), actual.getTriggerType());
    }

    private List<AgentOrder> getRandomAgentOrders() {
        List<AgentOrder> result = new ArrayList<AgentOrder>();

        int count = RANDOM.nextInt(5);

        for (int i = 0; i < count; i++) {
            AgentOrder agentOrder = new AgentOrder();

            agentOrder.setId(UUID.randomUUID().toString());
            if (RANDOM.nextBoolean()) {
                agentOrder.setTriggerParameter(Optional.of(UUID.randomUUID().toString()));
            } else {
                agentOrder.setTriggerParameter(Optional.<String> empty());
            }
            agentOrder.setTriggerType(TriggerType.values()[RANDOM.nextInt(TriggerType.values().length)]);

            result.add(agentOrder);
        }

        return result;
    }
}
