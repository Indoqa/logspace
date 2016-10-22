/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr;

import static com.indoqa.lang.util.StringUtils.escapeSolr;
import static com.indoqa.lang.util.TimeUtils.formatSolrDate;
import static io.logspace.hq.core.solr.EventFieldConstants.*;
import static io.logspace.hq.core.solr.utils.SolrQueryHelper.*;
import static org.apache.solr.common.params.CommonParams.SORT;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.StreamingResponseCallback;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.indoqa.lang.util.TimeUtils;

import io.logspace.agent.api.event.Event;
import io.logspace.hq.core.api.event.EventStreamService;
import io.logspace.hq.core.api.event.StoredEvent;
import io.logspace.hq.rest.api.EventStoreException;
import io.logspace.hq.rest.api.event.*;
import io.logspace.hq.rest.api.timeseries.TimeSeriesDefinition;
import io.logspace.hq.rest.api.timeseries.TimeWindow;

@Named
public class SolrEventStreamService implements EventStreamService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    @Qualifier("event-solr-client")
    private SolrClient solrClient;

    @Value("${logspace.solr.fallback-shard}")
    private String fallbackShard;

    @Override
    public void stream(EventFilter eventFilter, int count, int offset, EventStreamer eventStreamer) {
        SolrQuery solrQuery = new SolrQuery(ALL_DOCS_QUERY);
        solrQuery.setStart(offset);
        solrQuery.setRows(count);
        solrQuery.set(SORT, SORT_CRON_ASC);

        for (EventFilterElement eachElement : eventFilter) {
            solrQuery.addFilterQuery(this.createFilterQuery(eachElement));
        }

        try {
            this.solrClient.queryAndStreamResponse(solrQuery, new EventStreamCallback(eventStreamer));
        } catch (SolrServerException | IOException e) {
            String message = "Failed to stream events.";
            this.logger.error(message, e);
            throw EventStoreException.retrieveFailed(message, e);
        }
    }

    @Override
    public void stream(TimeSeriesDefinition definition, EventStreamer eventStreamer) {
        SolrQuery solrQuery = new SolrQuery(ALL_DOCS_QUERY);

        solrQuery.addFilterQuery(FIELD_GLOBAL_AGENT_ID + ":" + escapeSolr(definition.getGlobalAgentId()));
        solrQuery.addFilterQuery(this.getTimestampRangeQuery(definition.getTimeWindow()));

        try {
            this.solrClient.queryAndStreamResponse(solrQuery, new EventStreamCallback(eventStreamer));
        } catch (SolrServerException | IOException e) {
            String message = "Failed to stream events.";
            this.logger.error(message, e);
            throw EventStoreException.retrieveFailed(message, e);
        }
    }

    private void appendSolrValue(StringBuilder stringBuilder, Object value) {
        if (value == null) {
            stringBuilder.append('*');
            return;
        }

        if (value instanceof Date) {
            stringBuilder.append(TimeUtils.formatSolrDate((Date) value));
        }

        String result = String.valueOf(value);
        if (StringUtils.isBlank(result)) {
            stringBuilder.append('*');
            return;
        }

        stringBuilder.append('"');
        stringBuilder.append(result);
        stringBuilder.append('"');
    }

    private Event createEvent(SolrDocument solrDocument) {
        StoredEvent result = new StoredEvent();

        result.setId(this.getString(solrDocument, FIELD_ID));
        result.setSystem(this.getString(solrDocument, FIELD_SYSTEM));
        result.setAgentId(this.getString(solrDocument, FIELD_AGENT_ID));
        result.setType(this.getString(solrDocument, FIELD_TYPE));
        result.setMarker(this.getString(solrDocument, FIELD_MARKER));
        result.setTimestamp(this.getDate(solrDocument, FIELD_TIMESTAMP));
        result.setParentEventId(this.getString(solrDocument, FIELD_PARENT_ID));
        result.setGlobalEventId(this.getString(solrDocument, FIELD_GLOBAL_ID));

        for (Entry<String, Object> eachField : solrDocument) {
            String fieldName = eachField.getKey();

            if (fieldName.startsWith("boolean_property_")) {
                result.addProperties(fieldName.substring("boolean_property_".length()), eachField.getValue());
            }

            if (fieldName.startsWith("date_property_")) {
                result.addProperties(fieldName.substring("date_property_".length()), eachField.getValue());
            }

            if (fieldName.startsWith("double_property_")) {
                result.addProperties(fieldName.substring("double_property_".length()), eachField.getValue());
            }

            if (fieldName.startsWith("float_property_")) {
                result.addProperties(fieldName.substring("float_property_".length()), eachField.getValue());
            }

            if (fieldName.startsWith("integer_property_")) {
                result.addProperties(fieldName.substring("integer_property_".length()), eachField.getValue());
            }

            if (fieldName.startsWith("long_property_")) {
                result.addProperties(fieldName.substring("long_property_".length()), eachField.getValue());
            }

            if (fieldName.startsWith("string_property_")) {
                result.addProperties(fieldName.substring("string_property_".length()), eachField.getValue());
            }
        }

        return result;
    }

    private String createFilterQuery(EventFilterElement eventFilterElement) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(eventFilterElement.getProperty());
        stringBuilder.append(':');

        if (eventFilterElement instanceof EqualsEventFilterElement) {
            EqualsEventFilterElement equalsEventFilterElement = (EqualsEventFilterElement) eventFilterElement;
            this.appendSolrValue(stringBuilder, equalsEventFilterElement.getValue());
        }

        if (eventFilterElement instanceof RangeEventFilterElement) {
            RangeEventFilterElement rangeEventFilterElement = (RangeEventFilterElement) eventFilterElement;
            stringBuilder.append('[');
            this.appendSolrValue(stringBuilder, rangeEventFilterElement.getFrom());
            stringBuilder.append(" TO ");
            this.appendSolrValue(stringBuilder, rangeEventFilterElement.getTo());
            stringBuilder.append(']');
        }

        if (eventFilterElement instanceof MultiValueEventFilterElement) {
            MultiValueEventFilterElement multiValueEventFilterElement = (MultiValueEventFilterElement) eventFilterElement;
            stringBuilder.append('(');
            for (Iterator<String> iterator = multiValueEventFilterElement.getValues().iterator(); iterator.hasNext();) {
                this.appendSolrValue(stringBuilder, iterator.next());

                if (iterator.hasNext()) {
                    stringBuilder.append(' ');
                    stringBuilder.append(multiValueEventFilterElement.getOperator());
                    stringBuilder.append(' ');
                }
            }
            stringBuilder.append(')');
        }

        return stringBuilder.toString();
    }

    private Date getDate(SolrDocument solrDocument, String fieldName) {
        return (Date) solrDocument.getFieldValue(fieldName);
    }

    private String getString(SolrDocument solrDocument, String fieldName) {
        return (String) solrDocument.getFieldValue(fieldName);
    }

    private String getTimestampRangeQuery(Date start, Date end) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(FIELD_TIMESTAMP);
        stringBuilder.append(":[");
        stringBuilder.append(formatSolrDate(start));
        stringBuilder.append(" TO ");
        stringBuilder.append(formatSolrDate(end));
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    private String getTimestampRangeQuery(TimeWindow timeWindow) {
        return this.getTimestampRangeQuery(timeWindow.getStart(), timeWindow.getEnd());
    }

    private final class EventStreamCallback extends StreamingResponseCallback {

        private final EventStreamer eventStreamer;

        public EventStreamCallback(EventStreamer eventStreamer) {
            this.eventStreamer = eventStreamer;
        }

        @Override
        public void streamDocListInfo(long numFound, long start, Float maxScore) {
            // do nothing
        }

        @Override
        public void streamSolrDocument(SolrDocument solrDocument) {
            try {
                this.eventStreamer.streamEvent(SolrEventStreamService.this.createEvent(solrDocument));
            } catch (IOException e) {
                throw EventStoreException.retrieveFailed("Failed to stream events.", e);
            }
        }
    }
}
