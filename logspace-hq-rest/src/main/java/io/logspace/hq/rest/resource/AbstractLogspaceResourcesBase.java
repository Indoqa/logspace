/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.resource;

import org.springframework.beans.factory.annotation.Value;

import com.indoqa.boot.AbstractJsonResourcesBase;

public abstract class AbstractLogspaceResourcesBase extends AbstractJsonResourcesBase {

    @Value("${logspace.hq-rest.base-path}")
    private String basePath;

    @Override
    protected CharSequence getResourceBase() {
        return this.basePath;
    }
}
