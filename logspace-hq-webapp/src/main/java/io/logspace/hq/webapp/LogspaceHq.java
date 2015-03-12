/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp;

import java.io.IOException;

import spark.utils.IOUtils;

import com.indoqa.spark.AbstractSparkApplication;

public class LogspaceHq extends AbstractSparkApplication {

    private static final String APPLICATION_NAME = "Logspace";
    private static final String BASE_PACKAGE = "io.logspace";
    private static final String LOGO_PATH = "/logspace.io.txt";

    public static void main(String[] args) {
        new LogspaceHq().invoke();
    }

    @Override
    public void beforeInitialization() {
        this.printLogo();
    }

    @Override
    protected String getApplicationName() {
        return APPLICATION_NAME;
    }

    @Override
    protected String getComponentScanBasePackage() {
        return BASE_PACKAGE;
    }

    @Override
    protected void initializeSpringBeans() {
        // nothing to do yet
    }

    @Override
    protected boolean isDevEnvironment() {
        return true;
    }

    private void printLogo() {
        try {
            String asciiLogo = IOUtils.toString(LogspaceHq.class.getResourceAsStream(LOGO_PATH));
            if (asciiLogo == null) {
                return;
            }
            this.getInitialzationLogger().info(asciiLogo);
        } catch (IOException e) {
            throw new ApplicationInitializationException("Error while reading ASCII logo.", e);
        }
    }
}
