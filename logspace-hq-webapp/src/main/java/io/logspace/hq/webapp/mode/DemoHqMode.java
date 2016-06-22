/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp.mode;

import java.io.*;

import org.apache.commons.io.IOUtils;

import com.indoqa.boot.ApplicationInitializationException;

import io.logspace.agent.api.util.ConsoleWriter;

public class DemoHqMode implements HqMode {

    private static final String SPARK_DEFAULT_PORT = "4567";

    public DemoHqMode() {
        super();

        this.initializeDemoLogging();
    }

    @Override
    public void afterInitialization() {
        ConsoleWriter.write("Logspace HQ now running in demo mode");
        ConsoleWriter.write("Go to http://localhost:" + SPARK_DEFAULT_PORT);
        ConsoleWriter.write("");
    }

    @Override
    public void beforeInitialization() {
        this.initializeDemoPort();
        this.initializeDemoSolr();

        this.initializeDemoCapabilities();
    }

    private void createFile(String resourceDirectory, File outputDirectory, String fileName) {
        InputStream resourceStream = null;
        OutputStream outputStream = null;

        outputDirectory.mkdirs();

        try {
            resourceStream = this.getClass().getResourceAsStream(resourceDirectory + fileName);
            outputStream = new FileOutputStream(new File(outputDirectory, fileName));

            IOUtils.copy(resourceStream, outputStream);
        } catch (IOException e) {
            throw new ApplicationInitializationException(
                "Could not create file '" + fileName + "' in directory '" + outputDirectory + "'.", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(resourceStream);
        }
    }

    private File getBaseDir() {
        return new File(System.getProperty("java.io.tmpdir"), "logspace-demo");
    }

    private void initializeDemoCapabilities() {
        System.setProperty("logspace.hq-webapp.data-directory", this.getBaseDir().getAbsolutePath());

        File capabilitiesDir = new File(this.getBaseDir(), "capabilities");
        capabilitiesDir.mkdirs();

        File ordersDir = new File(this.getBaseDir(), "orders");
        this.createFile("/demo/orders/", ordersDir, "logspace-demo.json");
        this.createFile("/demo/orders/", ordersDir, "logspace-sample.json");

        File spacesDir = new File(this.getBaseDir(), "spaces");
        this.createFile("/demo/spaces/", spacesDir, "demo.space");
        this.createFile("/demo/spaces/", spacesDir, "logspace-sample.space");
    }

    private void initializeDemoLogging() {
        File logDir = new File(this.getBaseDir(), "logs");
        logDir.mkdirs();

        System.setProperty("log-path", logDir.getAbsolutePath());
        System.setProperty("log4j.configurationFile", "log4j2-demo.xml");
    }

    private void initializeDemoPort() {
        System.setProperty("port", String.valueOf(SPARK_DEFAULT_PORT));
    }

    private void initializeDemoSolr() {
        File solrDir = new File(this.getBaseDir(), "solr");
        System.setProperty("logspace.solr.base-url", solrDir.toURI().toString());
    }
}
