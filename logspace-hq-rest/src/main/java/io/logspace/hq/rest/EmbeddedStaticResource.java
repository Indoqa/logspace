/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest;

import static com.indoqa.boot.jsapp.WebpackAssetsUtils.findWebpackAssets;
import static spark.Spark.staticFileLocation;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;

import com.indoqa.boot.jsapp.AbstractJsAppResourcesBase;

@Profile("prod")
public class EmbeddedStaticResource extends AbstractJsAppResourcesBase {

    private static final String FRONTEND_MODULE = "/logspace-frontend-new";

    @PostConstruct
    public void mount() {
        staticFileLocation(FRONTEND_MODULE);

        this.jsApp("/", findWebpackAssets(FRONTEND_MODULE));
    }
}
