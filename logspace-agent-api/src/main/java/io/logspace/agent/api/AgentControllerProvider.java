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

import io.logspace.agent.api.util.ConsoleWriter;

/**
 * Factory for creating the {@link AgentController}.<br>
 * <br>
 * This class implements the lookup mechanism for find a {@link AgentControllerDescription} if none has been set programmatically:
 * <ol>
 * <li>Check the system-property 'logspace.config'. If a value is set, treat it as URL or file path and try to load the description
 * using {@link #setDescription(URL)}.</li>
 * <li>If the file './logspace.json' exists, try to load the description using {@link #setDescription(URL)}</li>
 * <li>If the resource '/logspace-test.json' exists in classpath, try to load the description using {@link #setDescription(URL)}</li>
 * <li>If the resource '/logspace.json' exists in classpath, try to load the description using {@link #setDescription(URL)}</li>
 * <li>If the resource '/logspace-default.json' exists in classpath, try to load the description using {@link #setDescription(URL)}
 * </li>
 * </ol>
 * If none of these locations exist, initializing an {@link AgentController} will fail.
 *
 * Setting an AgentControllerDescription programmatically will override the default lookup mechanism.
 */
public final class AgentControllerProvider {

    public static final String PROPERTY_LOGSPACE_CONFIG = "logspace.config";

    private static final String[] CONFIG_LOCATIONS = {"/logspace-test.json", "/logspace.json", "/logspace-default.json"};

    private static AgentController agentController;
    private static AgentControllerDescription agentControllerDescription;

    private AgentControllerProvider() {
        // hide utility class constructor
    }

    /**
     * Flush the current {@link AgentController}.<br>
     * This method does nothing if no AgentController is initialized.
     */
    public static synchronized void flush() {
        if (agentController != null) {
            agentController.flush();
        }
    }

    /**
     * Get the currently configured {@link AgentController}.<br>
     * This method will initialize the AgentController if necessary.
     *
     * @return The current AgentController.
     */
    public static synchronized AgentController getAgentController() {
        if (agentController == null) {
            agentController = initialize();
        }

        return agentController;
    }

    /**
     * @return <code>true</code> if an {@link AgentController} is initialized, <code>false</code> otherwise.
     */
    public static synchronized boolean isInitialized() {
        return agentController != null;
    }

    /**
     * Sets the {@link AgentControllerDescription} to be used for initializing the {@link AgentController}.<br>
     * <br>
     * This method will fail with an {@link AgentControllerInitializationException} if the AgentController has already been
     * initialized.<br>
     * Calling this method will <code>null</code> will revert back to the default lookup mechanism.
     *
     * @param agentControllerDescription The AgentControllerDescription to be set.
     */
    public static synchronized void setDescription(AgentControllerDescription agentControllerDescription) {
        if (isInitialized()) {
            throw new AgentControllerInitializationException(
                "Cannot set a new description after the AgentController has already been initialized.");
        }

        AgentControllerProvider.agentControllerDescription = agentControllerDescription;
    }

    /**
     * Set the {@link AgentControllerDescription} by reading it from the given {@link InputStream} and close the InputStream.<br>
     * <br>
     * This method expects the InputStream to provide a description in a format, that can be read by the
     * {@link AgentControllerDescriptionFactory}.<br>
     * This method will fail with an {@link AgentControllerInitializationException} if the AgentController has already been initialized
     * or the AgentControllerDescription could not be loaded from the InputStream.
     *
     * @param inputStream The InputStream to read the AgentControllerDescription from.
     */
    public static synchronized void setDescription(InputStream inputStream) {
        try {
            setDescription(AgentControllerDescriptionFactory.fromJson(inputStream));
        } catch (IOException ioex) {
            throw new AgentControllerInitializationException("Could not load logspace configuration.", ioex);
        } finally {
            closeQuietly(inputStream);
        }
    }

    /**
     * Set the {@link AgentControllerDescription} by reading it from the given {@link URL}.<br>
     * <br>
     * This method expects the URL to point to a description in a format, that can be read by the
     * {@link AgentControllerDescriptionFactory}.<br>
     * This method will fail with an {@link AgentControllerInitializationException} if the AgentController has already been initialized
     * or the {@link AgentControllerDescription} could not be loaded from the URL.
     *
     * @param descriptionURL The URL to read the AgentControllerDescription from.
     */
    public static synchronized void setDescription(URL descriptionURL) {
        if (descriptionURL == null) {
            return;
        }

        try {
            setDescription(descriptionURL.openStream());
            ConsoleWriter.writeSystem(MessageFormat.format("Loaded logspace configuration from ''{0}''.", descriptionURL));
        } catch (IOException ioex) {
            throw new AgentControllerInitializationException(
                "Could not load logspace configuration from URL '" + descriptionURL + "'.", ioex);
        }
    }

    /**
     * Flush the current {@link AgentController} and shut it down.<br>
     * After a successful call to this method, {@link #isInitialized()} will return <code>false</code> and a new
     * {@link AgentControllerDescription} can be set.<br>
     * A call to {@link #getAgentController()} will initialize a new AgentController, using the last AgentControllerDescription
     * configured. <br>
     * <br>
     * This method does nothing if no AgentController is initialized.
     */
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
            throw new AgentControllerInitializationException(
                "Failed to instantiate Agent Controller of class '" + constructor.getDeclaringClass() + "'.", e);
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

        throw new AgentControllerInitializationException(
            "Could not find a suitable constructor for AgentController '" + agentControllerClass + "'. Either a constructor accepting "
                + AgentControllerDescription.class + " or a default constructor is required.");
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

        if (!hasDescription()) {
            throw new AgentControllerInitializationException("Could not find any description.");
        }
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
            throw new AgentControllerInitializationException(
                "Could not load logspace configuration from the configured location '" + logspaceConfig + "'. Is the value correct?");
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
            throw new AgentControllerInitializationException("The Agent Controller class is unconfigured.");
        }

        try {
            return (Class<? extends AgentController>) Class.forName(description.getClassName());
        } catch (ClassNotFoundException cnfex) {
            throw new AgentControllerInitializationException(
                "Could not load Agent Controller class '" + description.getClassName() + "'.", cnfex);
        }
    }
}
