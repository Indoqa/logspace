/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.resource;

import static spark.Spark.staticFileLocation;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;

@Profile("prod")
public class EmbeddedStaticResources {

    @PostConstruct
    public void mount() {
        staticFileLocation("logspace-frontend");
    }
}
