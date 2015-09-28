/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp.mode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    }

    @Override
    public void beforeInitialization() {
        this.initializeDemoPort();
        this.initializeDemoSolr();

        this.initializeDemoCapabilities();
        this.initializeDemoConfigs();
        this.initializeDemoSpace();
    }

    private void createFile(String resourceDirectory, File outputDirectory, String fileName) {
        outputDirectory.mkdirs();

        InputStream resourceStream = null;
        OutputStream outputStream = null;

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
        File capabilitiesDir = new File(this.getBaseDir(), "capabilities");
        System.setProperty("logspace.hq-webapp.capabilities-directory", capabilitiesDir.getAbsolutePath());

        capabilitiesDir.mkdirs();
    }

    private void initializeDemoConfigs() {
        File ordersDir = new File(this.getBaseDir(), "orders");
        System.setProperty("logspace.hq-webapp.orders-directory", ordersDir.getAbsolutePath());

        ordersDir.mkdirs();
        this.createFile("/demo/orders/", ordersDir, "logspace-demo.json");
        this.createFile("/demo/orders/", ordersDir, "logspace-sample.json");
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

        File solrDataDir = new File(solrDir, "data");
        System.setProperty("logspace.solr.base-url", solrDataDir.toURI().toString());
        System.setProperty("logspace.solr.embedded-configuration-dir", solrDir.getAbsolutePath());

        File solrConfDir = new File(solrDir, "conf");
        this.createFile("/demo/conf/", solrConfDir, "elevate.xml");
        this.createFile("/demo/conf/", solrConfDir, "schema.xml");
        this.createFile("/demo/conf/", solrConfDir, "solrconfig.xml");
    }

    private void initializeDemoSpace() {
        File spaceTokensDir = new File(this.getBaseDir(), "space-tokens");
        System.setProperty("logspace.hq-webapp.space-tokens-directory", spaceTokensDir.getAbsolutePath());

        this.createFile("/demo/space-tokens/", spaceTokensDir, "demo.space-tokens");
        this.createFile("/demo/space-tokens/", spaceTokensDir, "logspace-sample.space-tokens");
    }
}
