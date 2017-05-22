/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp;

import static com.indoqa.boot.jsapp.WebpackAssetsUtils.findWebpackAssetsInClasspath;

import com.indoqa.boot.AbstractIndoqaBootApplication;

public class LogspaceHq extends AbstractIndoqaBootApplication {

    private static final String APPLICATION_NAME = "Logspace";

    private static final String BASE_PACKAGE = "io.logspace";
    private static final String LOGO_PATH = "/logspace.io.txt";
    private static final String FRONTEND_MODULE = "/logspace-frontend-new";

    public static void main(String[] args) {
        LogspaceStartupLifecycle lifecycle = new LogspaceStartupLifecycle(args);
        new LogspaceHq().invoke(lifecycle);
    }

    @Override
    protected String getApplicationName() {
        return APPLICATION_NAME;
    }

    @Override
    protected String getAsciiLogoPath() {
        return LOGO_PATH;
    }

    @Override
    protected String[] getComponentScanBasePackages() {
        return new String[] {BASE_PACKAGE};
    }

    @Override
    protected boolean isDevEnvironment() {
        return findWebpackAssetsInClasspath(FRONTEND_MODULE).isEmpty();
    }
}
