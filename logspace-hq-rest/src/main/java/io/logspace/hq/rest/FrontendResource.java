/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest;

import javax.annotation.PostConstruct;

import com.indoqa.boot.html.react.AbstractReactResourceBase;

public class FrontendResource extends AbstractReactResourceBase {

    private static final String CLASSPATH_LOCATION = "/logspace-frontend-new";
    private static final String FILESYSTEM_LOCATION = "../logspace-frontend-new/target";

    @PostConstruct
    public void mount() {
        this.html("/", CLASSPATH_LOCATION, FILESYSTEM_LOCATION);
    }

    // @Override
    // protected ProxyURLMappings getProxyURLMappings() {
    // return new ProxyURLMappings().add("logspaceBaseUrl", "");
    // }
}
