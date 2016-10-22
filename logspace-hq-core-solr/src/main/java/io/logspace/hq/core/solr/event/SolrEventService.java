/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr.event;

import static com.indoqa.lang.util.StringUtils.escapeSolr;
import static io.logspace.hq.core.solr.EventFieldConstants.*;
import static io.logspace.hq.core.solr.utils.SolrDocumentHelper.*;
import static io.logspace.hq.core.solr.utils.SolrQueryHelper.*;
import static java.util.Calendar.*;
import static java.util.stream.Collectors.*;
import static org.apache.solr.common.params.CommonParams.SORT;
import static org.apache.solr.common.params.CursorMarkParams.CURSOR_MARK_PARAM;
import static org.apache.solr.common.params.ShardParams._ROUTE_;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Value;

import com.indoqa.lang.util.TimeUtils;
import com.indoqa.solr.facet.api.*;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.agent.api.json.EventPage;
import io.logspace.agent.api.order.Aggregate;
import io.logspace.agent.api.order.PropertyDescription;
import io.logspace.hq.core.api.agent.IdHelper;
import io.logspace.hq.core.api.event.EventService;
import io.logspace.hq.core.api.event.StoredEvent;
import io.logspace.hq.core.solr.AbstractSolrEventService;
import io.logspace.hq.rest.api.DataDeletionException;
import io.logspace.hq.rest.api.DataRetrievalException;
import io.logspace.hq.rest.api.EventStoreException;
import io.logspace.hq.rest.api.event.*;
import io.logspace.hq.rest.api.timeseries.InvalidTimeSeriesDefinitionException;
import io.logspace.hq.rest.api.timeseries.TimeSeriesDefinition;

@Named
public class SolrEventService extends AbstractSolrEventService implements EventService {

    private static final long SLICE_UPDATE_INTERVAL = 1000L;

    @Value("${logspace.solr.fallback-shard}")
    private String fallbackShard;

    private Map<String, Slice> activeSlicesMap;
    private long nextSliceUpdate;

    @Override
    public void delete(List<String> ids) {
        this.logger.debug("Deleting events with IDs {}.", ids);

        try {
            this.solrClient.deleteById(ids);
        } catch (SolrServerException | IOException e) {
            throw new DataDeletionException("Could not delete events.", e);
        }
    }

    @Override
    public Object[] getTimeSeries(TimeSeriesDefinition dataDefinition) {
        SolrQuery solrQuery = new SolrQuery(ALL_DOCS_QUERY);
        solrQuery.setRows(0);

        solrQuery.addFilterQuery(FIELD_GLOBAL_AGENT_ID + ":" + escapeSolr(dataDefinition.getGlobalAgentId()));
        solrQuery.addFilterQuery(getTimestampRangeQuery(dataDefinition.getTimeWindow()));
        solrQuery.addFilterQuery(dataDefinition.getPropertyId() + ":*");

        for (String eachFilterQuery : this.createFilterQueries(dataDefinition.getFilter())) {
            solrQuery.addFilterQuery(eachFilterQuery);
        }

        solrQuery.set("json.facet", this.createTimeSeriesFacets(dataDefinition));

        try {
            QueryResponse response = this.solrClient.query(solrQuery, METHOD.POST);

            List<Object> values = new ArrayList<Object>();

            Buckets buckets = Buckets.fromResponse(response, FIELD_TIMESTAMP);
            for (NamedList<Object> eachBucket : buckets) {
                if (dataDefinition.getAggregate() == Aggregate.count) {
                    values.add(eachBucket.get(COUNT_FACET_NAME));
                } else {
                    values.add(eachBucket.get(AGGREGATION_FACET_NAME));
                }
            }

            return values.toArray();
        } catch (SolrException | SolrServerException | IOException e) {
            throw new DataRetrievalException("Could not retrieve data.", e);
        }
    }

    @Override
    public EventPage retrieve(EventFilter eventFilter, int count, String cursorMark) {
        return this.retrieve(eventFilter, count, cursorMark, SORT_CRON_ASC);
    }

    @Override
    public EventPage retrieveReversed(EventFilter eventFilter, int count, String cursorMark) {
        return this.retrieve(eventFilter, count, cursorMark, SORT_CRON_DESC);
    }

    @Override
    public void store(Collection<? extends Event> events, String space) {
        if (events == null || events.isEmpty()) {
            return;
        }

        String system = events.stream().findFirst().get().getSystem();
        this.logger.debug("Storing {} event(s) for space '{}' from system {}", events.size(), space, system);

        try {
            Collection<SolrInputDocument> inputDocuments = this.createInputDocuments(events, space);
            this.solrClient.add(inputDocuments);

            this.logger.info("Successfully stored {} event(s) for space '{}' from system {}", events.size(), space, system);
        } catch (SolrServerException | IOException e) {
            String message = "Failed to store " + events.size() + " events.";
            this.logger.error(message, e);
            throw EventStoreException.storeFailed(message, e);
        }
    }

    private void addProperties(SolrInputDocument document, Iterable<? extends EventProperty<?>> properties, String prefix) {
        for (EventProperty<?> eachProperty : properties) {
            String propertyId = prefix + eachProperty.getKey();

            document.addField(propertyId, eachProperty.getValue());
            document.addField(FIELD_PROPERTY_ID, propertyId);
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

        result.setId(getString(solrDocument, FIELD_ID));
        result.setSystem(getString(solrDocument, FIELD_SYSTEM));
        result.setAgentId(getString(solrDocument, FIELD_AGENT_ID));
        result.setType(getString(solrDocument, FIELD_TYPE));
        result.setMarker(getString(solrDocument, FIELD_MARKER));
        result.setTimestamp(getDate(solrDocument, FIELD_TIMESTAMP));
        result.setParentEventId(getString(solrDocument, FIELD_PARENT_ID));
        result.setGlobalEventId(getString(solrDocument, FIELD_GLOBAL_ID));

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

    private List<String> createFilterQueries(EventFilter filter) {
        if (filter == null) {
            return Collections.emptyList();
        }

        Stream<EventFilterElement> filterStream = StreamSupport.stream(filter.spliterator(), false);
        Collection<List<EventFilterElement>> groupedFilters = filterStream
            .collect(groupingBy(EventFilterElement::getProperty))
            .values();

        List<String> result = new ArrayList<>(groupedFilters.size());
        for (List<EventFilterElement> eachPropertyFilters : groupedFilters) {
            result.add(eachPropertyFilters.stream().map(this::createFilterQuery).collect(joining(" OR ")));
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

    private SolrInputDocument createInputDocument(Event event, String space) {
        SolrInputDocument result = new SolrInputDocument();

        String globalAgentId = IdHelper.getGlobalAgentId(space, event.getSystem(), event.getAgentId());

        result.addField(FIELD_ID, event.getId());
        result.addField(FIELD_GLOBAL_AGENT_ID, globalAgentId);
        result.addField(FIELD_SPACE, space);
        result.addField(FIELD_SYSTEM, event.getSystem());
        result.addField(FIELD_AGENT_ID, event.getAgentId());
        result.addField(FIELD_TYPE, event.getType());
        result.addField(FIELD_MARKER, event.getMarker());
        result.addField(FIELD_TIMESTAMP, event.getTimestamp());
        result.addField(FIELD_PARENT_ID, event.getParentEventId());
        result.addField(FIELD_GLOBAL_ID, event.getGlobalEventId());

        this.addProperties(result, event.getBooleanProperties(), "boolean_property_");
        this.addProperties(result, event.getDateProperties(), "date_property_");
        this.addProperties(result, event.getDoubleProperties(), "double_property_");
        this.addProperties(result, event.getFloatProperties(), "float_property_");
        this.addProperties(result, event.getIntegerProperties(), "integer_property_");
        this.addProperties(result, event.getLongProperties(), "long_property_");
        this.addProperties(result, event.getStringProperties(), "string_property_");

        if (this.isCloud) {
            result.setField(_ROUTE_, this.getTargetShard(event.getTimestamp()));
        }

        return result;
    }

    private Collection<SolrInputDocument> createInputDocuments(Collection<? extends Event> events, String space) {
        Collection<SolrInputDocument> result = new ArrayList<SolrInputDocument>();

        for (Event eachEvent : events) {
            result.add(this.createInputDocument(eachEvent, space));
        }

        return result;
    }

    private String createTimeSeriesFacets(TimeSeriesDefinition dataDefinition) {
        PropertyDescription propertyDescription = createPropertyDescription(dataDefinition.getPropertyId());
        if (!propertyDescription.getPropertyType().isAllowed(dataDefinition.getAggregate())) {
            throw InvalidTimeSeriesDefinitionException.illegalAggregate(propertyDescription.getPropertyType(),
                dataDefinition.getAggregate());
        }

        Date startDate = dataDefinition.getTimeWindow().getStart();
        Date endDate = dataDefinition.getTimeWindow().getEnd();
        int gap = dataDefinition.getTimeWindow().getGap();

        RangeFacet rangeFacet = new RangeFacet(FIELD_TIMESTAMP, FIELD_TIMESTAMP, startDate, endDate, GapUnit.SECONDS, gap);
        if (dataDefinition.getAggregate() != Aggregate.count) {
            rangeFacet.addSubFacet(new StatisticFacet(AGGREGATION_FACET_NAME, dataDefinition.getFacetFunction()));
        }

        return FacetList.toJsonString(rangeFacet);
    }

    private String getTargetShard(Date timestamp) {
        if (!this.isCloud) {
            return null;
        }

        CloudSolrClient cloudSolrClient = (CloudSolrClient) this.solrClient;

        if (System.currentTimeMillis() > this.nextSliceUpdate) {
            this.nextSliceUpdate = System.currentTimeMillis() + SLICE_UPDATE_INTERVAL;
            this.activeSlicesMap = cloudSolrClient
                .getZkStateReader()
                .getClusterState()
                .getCollection(cloudSolrClient.getDefaultCollection())
                .getActiveSlicesMap();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        String sliceName = MessageFormat.format("{0,number,0000}-{1,number,00}", calendar.get(YEAR), calendar.get(MONTH) + 1);

        if (this.activeSlicesMap.containsKey(sliceName)) {
            return sliceName;
        }

        return this.fallbackShard;
    }

    private EventPage retrieve(EventFilter eventFilter, int count, String cursorMark, String sort) {
        SolrQuery solrQuery = new SolrQuery(ALL_DOCS_QUERY);
        solrQuery.setRows(count);
        solrQuery.set(CURSOR_MARK_PARAM, cursorMark);
        solrQuery.set(SORT, sort);

        for (EventFilterElement eachElement : eventFilter) {
            solrQuery.addFilterQuery(this.createFilterQuery(eachElement));
        }

        try {
            EventPage result = new EventPage();

            QueryResponse response = this.solrClient.query(solrQuery);
            for (SolrDocument eachSolrDocument : response.getResults()) {
                result.addEvent(this.createEvent(eachSolrDocument));
            }

            result.setNextCursorMark(response.getNextCursorMark());
            result.setTotalCount(response.getResults().getNumFound());

            return result;
        } catch (SolrServerException | IOException | SolrException e) {
            String message = "Failed to retrieve events.";
            this.logger.error(message, e);
            throw EventStoreException.retrieveFailed(message, e);
        }
    }
}
