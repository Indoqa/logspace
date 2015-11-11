/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static io.logspace.agent.api.json.RandomHelper.getRandomBoolean;
import static io.logspace.agent.api.json.RandomHelper.getRandomDouble;
import static io.logspace.agent.api.json.RandomHelper.getRandomString;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import io.logspace.agent.api.order.*;
import io.logspace.agent.api.order.PropertyDescription.PropertyUnit;

public class AgentControllerCapabilitiesTest {

    private static PropertyDescription[] getRandomPropertyDescriptions() {
        List<PropertyDescription> result = new ArrayList<PropertyDescription>();

        int count = RandomHelper.getRandomCount(5);
        for (int i = 0; i < count; i++) {
            PropertyDescription propertyDescription = new PropertyDescription();

            propertyDescription.setName(getRandomString());
            propertyDescription.setPropertyType(RandomHelper.getRandomEnumValue(PropertyType.class));
            propertyDescription.setUnits(getRandomUnits());

            result.add(propertyDescription);
        }

        return result.toArray(new PropertyDescription[result.size()]);
    }

    private static TriggerType[] getRandomTriggerTypes() {
        List<TriggerType> result = new ArrayList<TriggerType>();

        for (TriggerType eachTriggerType : TriggerType.values()) {
            if (getRandomBoolean()) {
                result.add(eachTriggerType);
            }
        }

        return result.toArray(new TriggerType[result.size()]);
    }

    private static PropertyUnit[] getRandomUnits() {
        List<PropertyUnit> result = new ArrayList<PropertyUnit>();

        int count = RandomHelper.getRandomCount(5);
        for (int i = 0; i < count; i++) {
            PropertyUnit unit = new PropertyUnit();

            unit.setName(getRandomString());
            unit.setFactor(getRandomDouble());

            result.add(unit);
        }

        return result.toArray(new PropertyUnit[result.size()]);
    }

    @Test
    public void test() throws IOException {
        for (int i = 0; i < 100; i++) {
            AgentControllerCapabilities expected = new AgentControllerCapabilities();
            expected.setId(getRandomString());
            expected.setSystem(getRandomString());
            expected.setSpace(getRandomString());
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

        this.comparePropertyDescriptions(expectedAgentCapabilities.getPropertyDescriptions(),
            actualAgentCapabilities.getPropertyDescriptions());
    }

    private void compare(AgentControllerCapabilities expected, AgentControllerCapabilities actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getSystem(), actual.getSystem());
        assertEquals(expected.getAgentCapabilitiesCount(), actual.getAgentCapabilitiesCount());

        for (int i = 0; i < expected.getAgentCapabilitiesCount(); i++) {
            this.compare(expected.getAgentCapabilities().get(i), actual.getAgentCapabilities().get(i));
        }
    }

    private void comparePropertyDescription(PropertyDescription expected, PropertyDescription actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPropertyType(), actual.getPropertyType());

        this.comparePropertyUnits(expected.getUnits(), actual.getUnits());
    }

    private void comparePropertyDescriptions(PropertyDescription[] expected, PropertyDescription[] actual) {
        assertEquals(expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            this.comparePropertyDescription(expected[i], actual[i]);
        }
    }

    private void comparePropertyUnits(PropertyUnit[] expected, PropertyUnit[] actual) {
        if (expected == null || expected.length == 0) {
            if (actual == null || actual.length == 0) {
                return;
            }

            fail("Expected no PropertyUnits, but found " + actual.length);
            return;
        }

        assertEquals(expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getName(), actual[i].getName());
            assertEquals(expected[i].getFactor(), actual[i].getFactor(), 0.001d);
        }
    }

    private List<AgentCapabilities> getRandomAgentCapabilities() {
        List<AgentCapabilities> result = new ArrayList<AgentCapabilities>();

        int count = RandomHelper.getRandomCount(5);

        for (int i = 0; i < count; i++) {
            AgentCapabilities agentCapabilities = new AgentCapabilities();

            agentCapabilities.setId(UUID.randomUUID().toString());
            agentCapabilities.setType(UUID.randomUUID().toString());
            agentCapabilities.setSupportedTriggerTypes(getRandomTriggerTypes());
            agentCapabilities.setPropertyDescriptions(getRandomPropertyDescriptions());

            result.add(agentCapabilities);
        }

        return result;
    }
}
