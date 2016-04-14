/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;

import io.logspace.agent.api.event.Event;
import io.logspace.hq.core.api.event.EventService;
import io.logspace.hq.core.api.event.EventService.EventStreamer;
import io.logspace.hq.rest.api.timeseries.TimeSeriesDefinition;
import io.logspace.hq.rest.api.timeseries.TimeSeriesDefinitions;
import spark.Request;
import spark.Response;

@Named
public class DownloadResource extends AbstractSpaceResource {

    private static final String PARAMETER_COUNT = "count";
    private static final int DEFAULT_COUNT = 10;
    private static final int MIN_COUNT = 0;
    private static final int MAX_STREAM_COUNT = Integer.MAX_VALUE;

    private static final String PARAMETER_OFFSET = "offset";
    private static final int DEFAULT_STREAM_OFFSET = 0;
    private static final int MIN_OFFSET = 0;
    private static final int MAX_OFFSET = MAX_STREAM_COUNT;

    @Inject
    private EventService eventService;

    @PostConstruct
    public void mount() {
        this.post("/download", (req, res) -> this.postDownload(req, res));
    }

    private Object postDownload(Request req, Response res) throws IOException {
        TimeSeriesDefinitions definitions = this.getTransformer().toObject(req.body(), TimeSeriesDefinitions.class);

        String[] globalAgentIds = new String[definitions.getDefinitionCount()];
        int index = 0;
        for (TimeSeriesDefinition timeSeriesDefinition : definitions) {
            globalAgentIds[index++] = timeSeriesDefinition.getGlobalAgentId();
        }

        Set<String> eventPropertyNames = this.eventService.getEventPropertyNames(globalAgentIds);

        res.raw().setHeader("Content-Disposition", "attachment; filename=Events.csv");
        res.raw().setContentType("text/csv;charset=UTF-8");

        ServletOutputStream outputStream = res.raw().getOutputStream();
        this.writeHeader(eventPropertyNames, outputStream);

        for (TimeSeriesDefinition eachTimeSeries : definitions.getDefinitions()) {
            this.eventService.stream(eachTimeSeries, new CsvEventStreamer(outputStream, eventPropertyNames));
        }

        outputStream.close();
        return "";
    }

    private void writeHeader(Set<String> eventPropertyNames, ServletOutputStream outputStream) throws IOException {
        outputStream.write(EventCsvSerializer.generateHeader(eventPropertyNames));
    }

    private static final class CsvEventStreamer implements EventStreamer {

        private final OutputStream outputStream;
        private final Set<String> eventPropertyNames;

        public CsvEventStreamer(OutputStream outputStream, Set<String> eventPropertyNames) {
            super();
            this.outputStream = outputStream;
            this.eventPropertyNames = eventPropertyNames;
        }

        @Override
        public void streamEvent(Event event) throws IOException {
            EventCsvSerializer.eventToCsv(event, this.eventPropertyNames, this.outputStream);
        }
    }
}
