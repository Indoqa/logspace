/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.order.TriggerType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.Test;

public class AgentControllerCapabilitiesTest {

    private static final Random RANDOM = new Random();

    private static TriggerType[] getRandomTriggerTypes() {
        List<TriggerType> result = new ArrayList<TriggerType>();

        for (TriggerType eachTriggerType : TriggerType.values()) {
            if (RANDOM.nextBoolean()) {
                result.add(eachTriggerType);
            }
        }

        return result.toArray(new TriggerType[result.size()]);
    }

    @Test
    public void test() throws IOException {
        for (int i = 0; i < 100; i++) {
            AgentControllerCapabilities expected = new AgentControllerCapabilities();
            expected.setId(UUID.randomUUID().toString());
            expected.setAgentCapabilities(this.getRandomAgentCapabilities());

            String json = AgentControllerCapabilitiesJsonSerializer.toJson(expected);
            AgentControllerCapabilities actual = AgentControllerCapabilitiesJsonDeserializer.fromJson(json.getBytes("UTF-8"));

            this.compare(expected, actual);
        }
    }

    private void compare(AgentCapabilities expectedAgentCapabilities, AgentCapabilities actualAgentCapabilities) {
        assertEquals(expectedAgentCapabilities.getId(), actualAgentCapabilities.getId());
        assertEquals(expectedAgentCapabilities.getType(), actualAgentCapabilities.getType());
        assertArrayEquals(expectedAgentCapabilities.getSupportedTriggerTypes(), actualAgentCapabilities.getSupportedTriggerTypes());
    }

    private void compare(AgentControllerCapabilities expected, AgentControllerCapabilities actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAgentCapabilitiesCount(), actual.getAgentCapabilitiesCount());

        for (int i = 0; i < expected.getAgentCapabilitiesCount(); i++) {
            this.compare(expected.getAgentCapabilities().get(i), actual.getAgentCapabilities().get(i));
        }
    }

    private List<AgentCapabilities> getRandomAgentCapabilities() {
        List<AgentCapabilities> result = new ArrayList<AgentCapabilities>();

        int count = RANDOM.nextInt(5);

        for (int i = 0; i < count; i++) {
            AgentCapabilities agentCapabilities = new AgentCapabilities();

            agentCapabilities.setId(UUID.randomUUID().toString());
            agentCapabilities.setType(UUID.randomUUID().toString());
            agentCapabilities.setSupportedTriggerTypes(getRandomTriggerTypes());

            result.add(agentCapabilities);
        }

        return result;
    }
}