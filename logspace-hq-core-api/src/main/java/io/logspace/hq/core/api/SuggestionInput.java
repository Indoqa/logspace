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
    private String system;
    private String space;

    public String getPropertyId() {
        return this.propertyId;
    }

    public String getSpace() {
        return this.space;
    }

    public String getSystem() {
        return this.system;
    }

    public String getText() {
        return this.text;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public void setText(String text) {
        this.text = text;
    }
}
