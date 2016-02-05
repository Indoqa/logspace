/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp;

import java.io.IOException;

import com.indoqa.boot.AbstractIndoqaBootApplication;
import com.indoqa.boot.ApplicationInitializationException;

import io.logspace.hq.rest.EmbeddedStaticResource;
import io.logspace.hq.rest.ExternalStaticResource;
import io.logspace.hq.webapp.mode.DefaultHqMode;
import io.logspace.hq.webapp.mode.DemoHqMode;
import io.logspace.hq.webapp.mode.HqMode;
import spark.utils.IOUtils;

public class LogspaceHq extends AbstractIndoqaBootApplication {

    private static final String APPLICATION_NAME = "Logspace";
    private static final String BASE_PACKAGE = "io.logspace";
    private static final String LOGO_PATH = "/logspace.io.txt";
    private static final String FRONTEND_INDEX_HTML_PATH = "/logspace-frontend/index.html";

    private final HqMode hqMode;

    public LogspaceHq() {
        this(new DefaultHqMode());
    }

    public LogspaceHq(HqMode hqMode) {
        super();

        this.hqMode = hqMode;
    }

    public static void main(String[] args) {
        LogspaceHq logspaceHq;

        if (hasArgument(args, "--demo")) {
            logspaceHq = new LogspaceHq(new DemoHqMode());
        } else {
            logspaceHq = new LogspaceHq(new DefaultHqMode());
        }

        logspaceHq.invoke();
    }

    private static boolean hasArgument(String[] arguments, String argument) {
        for (String eachArgument : arguments) {
            if (eachArgument.equalsIgnoreCase(argument)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void afterInitialization() {
        this.hqMode.afterInitialization();
    }

    @Override
    protected void beforeInitialization() {
        this.printLogo();

        this.hqMode.beforeInitialization();
    }

    @Override
    protected String getApplicationName() {
        return APPLICATION_NAME;
    }

    @Override
    protected String[] getComponentScanBasePackages() {
        return new String[] {BASE_PACKAGE};
    }

    @Override
    protected void initializeSpringBeans() {
        this.getApplicationContext().register(EmbeddedStaticResource.class);
        this.getApplicationContext().register(ExternalStaticResource.class);
    }

    @Override
    protected boolean isDevEnvironment() {
        return LogspaceHq.class.getResourceAsStream(FRONTEND_INDEX_HTML_PATH) == null;
    }

    private void printLogo() {
        try {
            String asciiLogo = IOUtils.toString(LogspaceHq.class.getResourceAsStream(LOGO_PATH));
            if (asciiLogo == null) {
                return;
            }
            getInitializationLogger().info(asciiLogo);
        } catch (IOException e) {
            throw new ApplicationInitializationException("Error while reading ASCII logo.", e);
        }
    }
}
