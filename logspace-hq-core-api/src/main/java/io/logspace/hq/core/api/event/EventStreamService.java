/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.event;

import java.io.IOException;

import io.logspace.agent.api.event.Event;
import io.logspace.hq.rest.api.event.EventFilter;
import io.logspace.hq.rest.api.timeseries.TimeSeriesDefinition;

public interface EventStreamService {

    void stream(EventFilter eventFilter, int count, int offset, EventStreamer eventStreamer);

    void stream(TimeSeriesDefinition timeSeriesDefinition, EventStreamer eventStreamer);

    interface EventStreamer {

        void streamEvent(Event event) throws IOException;
    }
}
