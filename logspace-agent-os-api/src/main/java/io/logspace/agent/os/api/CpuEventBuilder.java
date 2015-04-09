/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.os.api;

import io.logspace.agent.api.event.AbstractEventBuilder;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.Optional;

public class CpuEventBuilder extends AbstractEventBuilder {

    public static final String TYPE = "os-cpu";
    public static final String PROPERTY_LOAD_AVERAGE = "load-average";

    public CpuEventBuilder() {
        super();
    }

    public CpuEventBuilder(Event parentEvent) {
        super(parentEvent);
    }

    /**
     * @param loadAverage The value of the average load.
     * @return This event builder.
     */
    public CpuEventBuilder setLoadAverage(double loadAverage) {
        this.addProperty(PROPERTY_LOAD_AVERAGE, loadAverage);
        return this;
    }

    /**
     * @see io.logspace.agent.api.event.AbstractEventBuilder#getType()
     */
    @Override
    protected Optional<String> getType() {
        return Optional.of(TYPE);
    }
}
