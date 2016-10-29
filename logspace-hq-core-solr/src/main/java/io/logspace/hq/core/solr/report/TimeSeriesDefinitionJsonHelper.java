/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr.report;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.logspace.hq.rest.api.timeseries.TimeSeriesDefinitions;

public final class TimeSeriesDefinitionJsonHelper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }

    private TimeSeriesDefinitionJsonHelper() {
        // hide utility class constructor
    }

    public static TimeSeriesDefinitions deserializeDefinitions(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, TimeSeriesDefinitions.class);
    }

    public static String serializeDefinitions(TimeSeriesDefinitions timeSeriesDefinitions) throws IOException {
        return OBJECT_MAPPER.writeValueAsString(timeSeriesDefinitions);
    }
}
