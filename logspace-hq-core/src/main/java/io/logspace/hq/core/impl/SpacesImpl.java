/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.impl;

import java.util.Map;

import io.logspace.hq.core.api.model.Spaces;

public class SpacesImpl implements Spaces {

    private Map<String, String> spaceTokens;

    @Override
    public String getSpaceForAuthenticationToken(String authenticationToken) {
        return this.spaceTokens.get(authenticationToken);
    }

    public Map<String, String> getSpaceTokens() {
        return this.spaceTokens;
    }

    public void setSpaceTokens(Map<String, String> spaceTokens) {
        this.spaceTokens = spaceTokens;
    }

}
