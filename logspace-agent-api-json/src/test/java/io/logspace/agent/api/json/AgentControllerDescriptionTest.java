/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static org.junit.Assert.assertEquals;
import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.api.AgentControllerDescription.Parameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.Test;

public class AgentControllerDescriptionTest {

    private static final Random RANDOM = new Random();

    @Test
    public void test() throws IOException {
        for (int i = 0; i < 100; i++) {
            AgentControllerDescription expected = new AgentControllerDescription();
            expected.setId(UUID.randomUUID().toString());
            expected.setClassName(UUID.randomUUID().toString());
            expected.setParameters(this.getRandomParameters());

            String json = AgentControllerDescriptionJsonSerializer.toJson(expected);
            AgentControllerDescription actual = AgentControllerDescriptionJsonDeserializer.fromJson(json.getBytes("UTF-8"));

            this.compare(expected, actual);
        }
    }

    private void compare(AgentControllerDescription expected, AgentControllerDescription actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getClassName(), actual.getClassName());

        for (int i = 0; i < expected.getParameterCount(); i++) {
            this.compare(expected.getParameters().get(i), actual.getParameters().get(i));
        }
    }

    private void compare(Parameter expected, Parameter actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getValue(), actual.getValue());
    }

    private List<Parameter> getRandomParameters() {
        List<Parameter> result = new ArrayList<Parameter>();

        int count = RANDOM.nextInt(5);

        for (int i = 0; i < count; i++) {
            Parameter parameter = new Parameter();

            parameter.setName(UUID.randomUUID().toString());
            parameter.setValue(UUID.randomUUID().toString());

            result.add(parameter);
        }

        return result;
    }
}