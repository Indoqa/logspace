/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp;

import java.util.Arrays;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.indoqa.boot.application.AbstractStartupLifecycle;

import io.logspace.hq.rest.FrontendResource;
import io.logspace.hq.webapp.mode.DefaultHqMode;
import io.logspace.hq.webapp.mode.DemoHqMode;
import io.logspace.hq.webapp.mode.HqMode;

public class LogspaceStartupLifecycle extends AbstractStartupLifecycle {

    private final HqMode hqMode;

    public LogspaceStartupLifecycle(String[] args) {
        if (hasArgument(args, "--demo")) {
            this.hqMode = new DemoHqMode();
        } else {
            this.hqMode = new DefaultHqMode();
        }
    }

    private static boolean hasArgument(String[] arguments, String argument) {
        return Arrays.stream(arguments).anyMatch(eachArgument -> eachArgument.equalsIgnoreCase(argument));
    }

    @Override
    public void didInitializeSpring(AnnotationConfigApplicationContext context) {
        this.hqMode.didInitialize(context);
    }

    @Override
    public void willCreateDefaultSparkRoutes(AnnotationConfigApplicationContext context) {
        context.register(FrontendResource.class);
    }

    @Override
    public void willInitialize() {
        this.hqMode.willInitialize();
    }
}
