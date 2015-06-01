/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event.context;

public class DefaultEventContext implements EventContext {

    private String globalId;
    private String parentId;

    public DefaultEventContext() {
        super();
    }

    public DefaultEventContext(String globalId, String parentId) {
        this.globalId = globalId;
        this.parentId = parentId;
    }

    @Override
    public String getGlobalId() {
        return this.globalId;
    }

    @Override
    public String getParentId() {
        return this.parentId;
    }

    @Override
    public boolean hasGlobalId() {
        return this.globalId != null;
    }

    @Override
    public boolean hasParentId() {
        return this.parentId != null;
    }

    @Override
    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    @Override
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
