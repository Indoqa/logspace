/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

/**
 * Default event builder implementation which sets an empty type.
 */
public class DefaultEventBuilder extends AbstractEventBuilder {

    public DefaultEventBuilder(String agentId, String system, String marker) {
        super(agentId, system, marker);
    }

    @Override
    protected String getType() {
        return null;
    }
}
