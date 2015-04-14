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
import io.logspace.agent.api.json.AgentControllerDescriptionJsonDeserializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AgentControllerProvider {

    private static final String[] CONFIG_LOCATIONS = {"/logspace-test.json", "/logspace.json", "/logspace-default.json"};

    private static AgentController agentController;
    private static AgentControllerDescription agentControllerDescription;

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentControllerProvider.class);

    private AgentControllerProvider() {
        // hide utility class constructor
    }

    public static synchronized void flush() {
        if (agentController != null) {
            agentController.flush();
        }
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

    public static synchronized void setDescription(AgentControllerDescription agentControllerDescription) {
        if (isInitialized()) {
            throw new AgentControllerException("Cannot set a new description after the AgentController has already been initialized.");
        }

        AgentControllerProvider.agentControllerDescription = agentControllerDescription;
    }

    public static synchronized void setDescription(InputStream inputStream) {
        try {
            setDescription(AgentControllerDescriptionJsonDeserializer.fromJson(inputStream));
        } catch (IOException ioex) {
            throw new AgentControllerException("Could not load description.", ioex);
        } finally {
            closeQuietly(inputStream);
        }
    }

    public static synchronized void setDescription(URL descriptionURL) {
        if (descriptionURL == null) {
            return;
        }

        try {
            LOGGER.info("Loading AgentControllerDescription from {}.", descriptionURL);
            setDescription(descriptionURL.openStream());
        } catch (IOException ioex) {
            throw new AgentControllerException("Could not load description from URL '" + descriptionURL + "'.", ioex);
        }
    }

    public static synchronized void shutdown() {
        if (agentController == null) {
            return;
        }

        agentController.flush();
        agentController.shutdown();
        agentController = null;
    }

    private static void closeQuietly(InputStream inputStream) {
        if (inputStream == null) {
            return;
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            // do nothing
        }
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
        initializeDescription();

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

        throw new AgentControllerException("Could not find a suitable constructor for AgentController '" + agentControllerClass
                + "'. Either a constructor accepting " + AgentControllerDescription.class + " or a default constructor is required.");
    }

    private static void initializeDescription() {
        if (agentControllerDescription != null) {
            return;
        }

        File file = new File("logspace.json");
        if (file.exists()) {
            try {
                setDescription(file.toURI().toURL());
            } catch (MalformedURLException e) {
                // do nothing
            }
        } else {
            System.out.println(file.getAbsolutePath() + " does not exists.");
        }

        if (agentControllerDescription != null) {
            return;
        }

        for (String eachConfigLocation : CONFIG_LOCATIONS) {
            if (agentControllerDescription != null) {
                break;
            }

            setDescription(AgentControllerProvider.class.getResource(eachConfigLocation));
        }
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
}