/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api;

public class SuggestionInput {

    private String text;
    private String propertyId;
    private String systemId;
    private String spaceId;

    public String getPropertyId() {
        return this.propertyId;
    }

    public String getSpaceId() {
        return this.spaceId;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public String getText() {
        return this.text;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public void setText(String text) {
        this.text = text;
    }
}
