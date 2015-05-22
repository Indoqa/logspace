/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;

public final class AgentControllerProvider {

    private static final String PROPERTY_LOGSPACE_CONFIG = "logspace.config";

    private static final String[] CONFIG_LOCATIONS = {"/logspace-test.json", "/logspace.json", "/logspace-default.json"};

    private static AgentController agentController;
    private static AgentControllerDescription agentControllerDescription;

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
            setDescription(AgentControllerDescriptionFactory.fromJson(inputStream));
        } catch (IOException ioex) {
            throw new AgentControllerException("Could not load logspace configuration.", ioex);
        } finally {
            closeQuietly(inputStream);
        }
    }

    public static synchronized void setDescription(URL descriptionURL) {
        if (descriptionURL == null) {
            return;
        }

        try {
            setDescription(descriptionURL.openStream());
            System.out.println(MessageFormat.format("Loaded logspace configuration from ''{0}''.", descriptionURL));
        } catch (IOException ioex) {
            throw new AgentControllerException("Could not load logspace configuration from URL '" + descriptionURL + "'.", ioex);
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

    private static boolean hasDescription() {
        return agentControllerDescription != null;
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
        if (hasDescription()) {
            return;
        }

        initializeDescriptionFromSetting();

        if (hasDescription()) {
            return;
        }

        initializeDescriptionFromFile();

        if (hasDescription()) {
            return;
        }

        initializeDescriptionFromClasspath();
    }

    private static void initializeDescriptionFromClasspath() {
        for (String eachConfigLocation : CONFIG_LOCATIONS) {
            if (hasDescription()) {
                return;
            }

            setDescription(AgentControllerProvider.class.getResource(eachConfigLocation));
        }
    }

    private static void initializeDescriptionFromFile() {
        File file = new File("logspace.json");
        if (!file.exists()) {
            return;
        }

        try {
            setDescription(file.toURI().toURL());
        } catch (MalformedURLException e) {
            // do nothing
        }
    }

    private static void initializeDescriptionFromSetting() {
        String logspaceConfig = System.getProperty(PROPERTY_LOGSPACE_CONFIG);
        if (logspaceConfig == null) {
            return;
        }

        try {
            setDescription(new URL(logspaceConfig));
        } catch (MalformedURLException e) {
            // do nothing
        }

        if (hasDescription()) {
            return;
        }

        // assume the location is a file path
        File file = new File(logspaceConfig);
        if (!file.exists()) {
            throw new AgentControllerInitializationException("Could not load logspace configuration from the configured location '"
                    + logspaceConfig + "'. Is the value correct?");
        }

        try {
            setDescription(file.toURI().toURL());
        } catch (MalformedURLException e) {
            // do nothing
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