/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import java.io.IOException;
import java.io.InputStream;

/**
 * A factory for creating {@link AgentControllerDescription AgentControllerDescriptions}.<br>
 * <br>
 * Internally, this factory uses an {@link AgentControllerDescriptionDeserializer} to read AgentControllerDescriptions from an
 * {@link InputStream}. The actual implementation to be used can be configured with the system-property
 * 'logspace.configuration-deserializer'.<br>
 * If no implementation is configured, this factory will fall back to using the
 * {@link io.logspace.agent.api.json.AgentControllerDescriptionJsonDeserializer}
 */
public final class AgentControllerDescriptionFactory {

    private static final String IMPLEMENTATION_PROPERTY_NAME = "logspace.configuration-deserializer";

    private static final String DEFAULT_IMPLEMENTATION = "io.logspace.agent.api.json.AgentControllerDescriptionJsonDeserializer";

    private AgentControllerDescriptionFactory() {
        // hide utility class constructor
    }

    public static AgentControllerDescription fromJson(InputStream inputStream) throws IOException {
        AgentControllerDescriptionDeserializer deserializer = getDeserializer();
        return deserializer.read(inputStream);
    }

    @SuppressWarnings("unchecked")
    public static AgentControllerDescriptionDeserializer getDeserializer() {
        String deserializerClassName = System.getProperty(IMPLEMENTATION_PROPERTY_NAME, DEFAULT_IMPLEMENTATION);

        try {
            Class<? extends AgentControllerDescriptionDeserializer> deserializerClass = (Class<? extends AgentControllerDescriptionDeserializer>) Class
                .forName(deserializerClassName);

            return deserializerClass.newInstance();
        } catch (ClassNotFoundException e) {
            if (DEFAULT_IMPLEMENTATION.equals(deserializerClassName)) {
                throw new AgentControllerInitializationException("Could not load class '" + deserializerClassName
                    + "' as deserializer for the logspace configuration. Is logspace-agent-json.jar part of the classpath?", e);
            }

            throw new AgentControllerInitializationException("Could not find class '" + deserializerClassName
                + "' as deserializer for the logspace configuration. Did you configure '" + IMPLEMENTATION_PROPERTY_NAME
                + "' properly?", e);
        } catch (InstantiationException e) {
            throw new AgentControllerInitializationException("Could not instantiate class '" + deserializerClassName
                + "' as deserializer for the logspace configuration. Does this class have a default constructor?", e);
        } catch (IllegalAccessException e) {
            throw new AgentControllerInitializationException("Could not access the constructor of class '" + deserializerClassName
                + "' as deserializer for the logspace configuration. Does this class have a default constructor?", e);
        }
    }
}
