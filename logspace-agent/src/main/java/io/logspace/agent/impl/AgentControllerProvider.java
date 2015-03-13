/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.impl;

import io.logspace.agent.api.AgentController;
import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.api.json.AgentControllerDescriptionJsonSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.Arrays;

public final class AgentControllerProvider {

    private static AgentController agentController;
    private static AgentControllerDescription agentControllerDescription;

    private static boolean isShutdown;

    private AgentControllerProvider() {
        // hide utility class constructor
    }

    public static synchronized void flush() {
        verifyNotShutdown();

        agentController.flush();
    }

    public static synchronized AgentController getAgentController() {
        if (agentController == null) {
            agentController = initialize();
        }

        return agentController;
    }

    public static synchronized boolean isInitialized() {
        return agentController != null;
    }

    public static synchronized boolean isShutdown() {
        return isShutdown;
    }

    public static synchronized void setDescription(AgentControllerDescription agentControllerDescription) {
        verifyNotInitialized();

        AgentControllerProvider.agentControllerDescription = agentControllerDescription;
    }

    public static synchronized void setDescription(InputStream descriptionInputStream) {
        try {
            setDescription(AgentControllerDescriptionJsonSerializer.fromJson(descriptionInputStream));
        } catch (IOException ioex) {
            throw new AgentControllerException("Could not load description from InputStream.", ioex);
        } finally {
            try {
                descriptionInputStream.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    public static synchronized void setDescription(URI descriptionURI) {
        try {
            setDescription(descriptionURI.toURL().openStream());
        } catch (IOException ioex) {
            throw new AgentControllerException("Could not load description from URI '" + descriptionURI + "'.", ioex);
        }
    }

    public static synchronized void shutdown() {
        if (agentController != null) {
            agentController.shutdown();
            agentController = null;
        }

        isShutdown = true;
    }

    private static AgentControllerDescription createDefaultDescription() {
        return AgentControllerDescription.withClass(LoggingAgentController.class);
    }

    private static Object executeConstructor(Constructor<?> constructor, Object... arguments) {
        try {
            return constructor.newInstance(arguments);
        } catch (Exception e) {
            throw new AgentControllerException("Failed to instantiate Agent Controller of class '" + constructor.getDeclaringClass()
                    + "'.", e);
        }
    }

    private static Constructor<?> getConstructor(Constructor<?>[] constructors, Class<?>... parameterTypes) {
        for (Constructor<?> eachConstructor : constructors) {
            if (Arrays.equals(eachConstructor.getParameterTypes(), parameterTypes)) {
                return eachConstructor;
            }
        }

        return null;
    }

    private static AgentController initialize() {
        verifyNotShutdown();

        if (agentControllerDescription == null) {
            agentControllerDescription = createDefaultDescription();
        }

        Class<? extends AgentController> agentControllerClass = loadClass(agentControllerDescription);

        Constructor<?>[] constructors = agentControllerClass.getConstructors();
        Constructor<?> constructor = getConstructor(constructors, AgentControllerDescription.class);
        if (constructor != null) {
            return (AgentController) executeConstructor(constructor, agentControllerDescription);
        }

        constructor = getConstructor(constructors);
        if (constructor != null) {
            return (AgentController) executeConstructor(constructor);
        }

        throw new AgentControllerException("Could not find a suitable constructor for Agent Controller class '" + agentControllerClass
                + "'. Either a constructor accepting " + AgentControllerDescription.class + " or a default constructor is required.");
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends AgentController> loadClass(AgentControllerDescription description) {
        if (description.getClassName() == null) {
            throw new AgentControllerException("The Agent Controller class is unconfigured.");
        }

        try {
            return (Class<? extends AgentController>) Class.forName(description.getClassName());
        } catch (ClassNotFoundException cnfex) {
            throw new AgentControllerException("Could not load Agent Controller class '" + description.getClassName() + "'.", cnfex);
        }
    }

    private static void verifyNotInitialized() {
        verifyNotShutdown();

        if (isInitialized()) {
            throw new AgentControllerException("Cannot set a new description after the AgentController has already been initialized.");
        }
    }

    private static void verifyNotShutdown() {
        if (isShutdown()) {
            throw new AgentControllerException("The AgentControllerProvider is already shutdown.");
        }
    }
}