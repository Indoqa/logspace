/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import static com.indoqa.commons.lang.util.StringUtils.escapeSolr;
import static com.indoqa.commons.lang.util.TimeUtils.formatSolrDate;
import static java.util.Calendar.*;
import static java.util.concurrent.TimeUnit.*;
import static org.apache.solr.common.params.CommonParams.SORT;
import static org.apache.solr.common.params.CursorMarkParams.CURSOR_MARK_PARAM;
import static org.apache.solr.common.params.ShardParams._ROUTE_;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.AutoCloseInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.StreamingResponseCallback;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.InputStreamResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.response.JSONResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.indoqa.commons.lang.util.TimeTracker;
import com.indoqa.commons.lang.util.TimeUtils;
import com.indoqa.solr.facet.api.*;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.agent.api.json.EventPage;
import io.logspace.agent.api.order.Aggregate;
import io.logspace.agent.api.order.PropertyDescription;
import io.logspace.agent.api.order.PropertyType;
import io.logspace.hq.core.api.capabilities.CapabilitiesService;
import io.logspace.hq.core.api.event.EventService;
import io.logspace.hq.core.api.event.NativeQueryResult;
import io.logspace.hq.core.api.event.StoredEvent;
import io.logspace.hq.rest.api.DataRetrievalException;
import io.logspace.hq.rest.api.EventStoreException;
import io.logspace.hq.rest.api.agentactivity.AgentActivities;
import io.logspace.hq.rest.api.agentactivity.AgentActivity;
import io.logspace.hq.rest.api.event.*;
import io.logspace.hq.rest.api.suggestion.AgentDescription;
import io.logspace.hq.rest.api.suggestion.Suggestion;
import io.logspace.hq.rest.api.suggestion.SuggestionInput;
import io.logspace.hq.rest.api.timeseries.InvalidTimeSeriesDefinitionException;
import io.logspace.hq.rest.api.timeseries.TimeSeriesDefinition;
import io.logspace.hq.rest.api.timeseries.TimeWindow;

@Named
public class SolrEventService implements EventService {

    private static final String ALL_DOCS_QUERY = "*:*";
    private static final String SORT_CRON_ASC = "timestamp ASC, id ASC";
    private static final String SORT_CRON_DESC = "timestamp DESC, id ASC";

    private static final long AGENT_DESCRIPTION_REFRESH_INTERVAL = 60000L;
    private static final long SLICE_UPDATE_INTERVAL = 1000L;

    private static final String VALUE_FACET_NAME = "val";
    private static final String AGGREGATION_FACET_NAME = "agg";
    private static final String COUNT_FACET_NAME = "count";

    private static final String FIELD_ID = "id";
    private static final String FIELD_SPACE = "space";
    private static final String FIELD_SYSTEM = "system";
    private static final String FIELD_AGENT_ID = "agent_id";
    private static final String FIELD_GLOBAL_AGENT_ID = "global_agent_id";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_MARKER = "marker";
    private static final String FIELD_GLOBAL_ID = "global_id";
    private static final String FIELD_PARENT_ID = "parent_id";
    private static final String FIELD_TIMESTAMP = "timestamp";
    private static final String FIELD_PROPERTY_ID = "property_id";
    private static final String FIELD_TOKENIZED_SEARCH_FIELD = "tokenized_search_field";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    @Qualifier("logspace-solr-client")
    private SolrClient solrClient;

    @Inject
    private CapabilitiesService capabilitiesService;

    @Value("${logspace.solr.fallback-shard}")
    private String fallbackShard;

    private boolean isCloud;

    private Map<String, Slice> activeSlicesMap;
    private long nextSliceUpdate;
    private final Map<String, AgentDescription> cachedAgentDescriptions = new ConcurrentHashMap<>();

    private final JSONResponseWriter jsonResponseWriter = new JSONResponseWriter();

    @Override
    public NativeQueryResult executeNativeQuery(Map<String, String[]> parameters) {
        SolrParams params = this.createSolrParams(parameters);

        try {
            QueryRequest request = new QueryRequest(params, METHOD.POST);
            request.setResponseParser(new InputStreamResponseParser("json"));
            QueryResponse response = request.process(this.solrClient);

            InputStream inputStream = (InputStream) response.getResponse().get("stream");
            if (inputStream != null) {
                return new SolrNativeQueryResult(inputStream);
            }

            return new SolrNativeQueryResult(this.serializeResponse(params, response));
        } catch (SolrException | SolrServerException | IOException e) {
            throw new DataRetrievalException("Could not execute direct query with parameters " + parameters.toString() + ".", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public AgentActivities getAgentActivities(int start, int count, int durationSeconds, int steps, String sort) {
        SolrQuery solrQuery = new SolrQuery(ALL_DOCS_QUERY);
        solrQuery.setRows(0);

        Date endDate = new Date();
        Date startDate = new Date(endDate.getTime() - SECONDS.toMillis(durationSeconds));

        solrQuery.addFilterQuery(this.getTimestampRangeQuery(startDate, endDate));

        TermsFacet agentFacet = new TermsFacet(FIELD_GLOBAL_AGENT_ID, FIELD_GLOBAL_AGENT_ID);
        agentFacet.setOffset(start);
        agentFacet.setLimit(count);
        agentFacet.setMincount(0);
        agentFacet.setNumBuckets(true);
        agentFacet.setSort(sort);
        agentFacet.addSubFacet(
            new RangeFacet(FIELD_TIMESTAMP, FIELD_TIMESTAMP, startDate, endDate, GapUnit.SECONDS, durationSeconds / steps));

        solrQuery.set("json.facet", FacetList.toJsonString(agentFacet));

        try {
            AgentActivities result = new AgentActivities();

            QueryResponse response = this.solrClient.query(solrQuery, METHOD.POST);

            Buckets agentBuckets = Buckets.fromResponse(response, FIELD_GLOBAL_AGENT_ID);
            result.setOffset(start);
            result.setTotalCount(agentBuckets.getNumBuckets());

            int maxHistoryValue = 0;

            for (NamedList<Object> eachAgentBucket : agentBuckets) {
                AgentActivity agentActivity = new AgentActivity();

                agentActivity.setGlobalAgentId((String) eachAgentBucket.get(VALUE_FACET_NAME));
                agentActivity.setEventCount(Buckets.getInt(eachAgentBucket, COUNT_FACET_NAME));

                int[] history = new int[steps];

                Buckets historyBuckets = Buckets.fromFacet((NamedList<Object>) eachAgentBucket.get(FIELD_TIMESTAMP));
                for (int i = 0; i < Math.min(historyBuckets.getBucketCount(), history.length); i++) {
                    NamedList<Object> historyBucket = historyBuckets.getBucket(i);

                    int historyValue = Buckets.getInt(historyBucket, COUNT_FACET_NAME);
                    history[i] = historyValue;
                    maxHistoryValue = Math.max(maxHistoryValue, historyValue);
                }
                agentActivity.setHistory(history);

                result.add(agentActivity);
            }

            result.setMaxHistoryValue(maxHistoryValue);

            return result;
        } catch (SolrException | SolrServerException | IOException e) {
            throw new DataRetrievalException("Could not retrieve Agent activities.", e);
        }
    }

    @Override
    public Suggestion getSuggestion(SuggestionInput input) {
        TimeTracker timeTracker = new TimeTracker();

        SolrQuery solrQuery = new SolrQuery(ALL_DOCS_QUERY);
        solrQuery.setRows(0);

        if (!StringUtils.isBlank(input.getText())) {
            solrQuery.addFilterQuery(FIELD_TOKENIZED_SEARCH_FIELD + ":" + escapeSolr(input.getText()) + "*");
        }

        this.addFilterQuery(solrQuery, FIELD_PROPERTY_ID, input.getPropertyId());
        this.addFilterQuery(solrQuery, FIELD_SPACE, input.getSpaceId());
        this.addFilterQuery(solrQuery, FIELD_SYSTEM, input.getSystemId());

        solrQuery.setFacetMinCount(1);
        solrQuery.addFacetField(FIELD_GLOBAL_AGENT_ID);

        try {
            Suggestion result = new Suggestion();

            QueryResponse response = this.solrClient.query(solrQuery);

            FacetField globalAgentIdFacetField = response.getFacetField(FIELD_GLOBAL_AGENT_ID);
            for (Count eachValue : globalAgentIdFacetField.getValues()) {
                String globalAgentId = eachValue.getName();

                result.addAgentDescription(this.getAgentDescription(globalAgentId));
            }

            result.setExecutionTime(timeTracker.getElapsed(MILLISECONDS));
            return result;
        } catch (SolrException | SolrServerException | IOException e) {
            throw new DataRetrievalException("Failed to create suggestions", e);
        }
    }

    @Override
    public Object[] getTimeSeries(TimeSeriesDefinition dataDefinition) {
        SolrQuery solrQuery = new SolrQuery(ALL_DOCS_QUERY);
        solrQuery.setRows(0);

        solrQuery.addFilterQuery(FIELD_GLOBAL_AGENT_ID + ":" + escapeSolr(dataDefinition.getGlobalAgentId()));
        solrQuery.addFilterQuery(this.getTimestampRangeQuery(dataDefinition.getTimeWindow()));
        solrQuery.addFilterQuery(dataDefinition.getPropertyId() + ":*");
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

    @PostConstruct
    public void initialize() {
        this.isCloud = this.solrClient instanceof CloudSolrClient;

        if (this.isCloud) {
            ((CloudSolrClient) this.solrClient).connect();
        }

        new Timer(true).schedule(new RefreshAgentDescriptionCacheTask(), AGENT_DESCRIPTION_REFRESH_INTERVAL,
            AGENT_DESCRIPTION_REFRESH_INTERVAL);
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

    protected void refreshAgentDescriptionCache() {
        for (String eachGlobalAgentId : this.cachedAgentDescriptions.keySet()) {
            try {
                this.cachedAgentDescriptions.put(eachGlobalAgentId, this.loadAgentDescription(eachGlobalAgentId));
            } catch (Exception e) {
                this.cachedAgentDescriptions.remove(eachGlobalAgentId);
            }
        }
    }

    private void addFilterQuery(SolrQuery solrQuery, String fieldName, String value) {
        if (StringUtils.isBlank(value)) {
            return;
        }

        solrQuery.addFilterQuery(fieldName + ":" + escapeSolr(value));
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

    private SolrInputDocument createInputDocument(Event event, String space) {
        SolrInputDocument result = new SolrInputDocument();

        result.addField(FIELD_ID, event.getId());

        result.addField(FIELD_GLOBAL_AGENT_ID,
            this.capabilitiesService.getGlobalAgentId(space, event.getSystem(), event.getAgentId()));
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

    private PropertyDescription createPropertyDescription(String propertyId) {
        if (propertyId == null) {
            return null;
        }

        Pattern propertyIdPattern = Pattern.compile("(\\w+)_property_(.*?)");
        Matcher matcher = propertyIdPattern.matcher(propertyId);
        if (!matcher.matches()) {
            return null;
        }

        PropertyDescription result = new PropertyDescription();

        result.setId(propertyId);
        result.setPropertyType(PropertyType.get(matcher.group(1)));
        result.setName(matcher.group(2));

        return result;
    }

    private SolrParams createSolrParams(Map<String, String[]> parameters) {
        ModifiableSolrParams result = new ModifiableSolrParams();

        for (Entry<String, String[]> eachEntry : parameters.entrySet()) {
            result.add(eachEntry.getKey(), eachEntry.getValue());
        }

        return result;
    }

    private String createTimeSeriesFacets(TimeSeriesDefinition dataDefinition) {
        PropertyDescription propertyDescription = this.createPropertyDescription(dataDefinition.getPropertyId());
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

    private AgentDescription getAgentDescription(String globalAgentId) throws SolrServerException, IOException {
        AgentDescription agentDescription = this.capabilitiesService.getAgentDescription(globalAgentId);

        if (agentDescription == null || agentDescription.getPropertyDescriptions() == null
            || agentDescription.getPropertyDescriptions().isEmpty()) {
            agentDescription = this.cachedAgentDescriptions.get(globalAgentId);
        }

        if (agentDescription == null) {
            agentDescription = this.loadAgentDescription(globalAgentId);
            this.cachedAgentDescriptions.put(globalAgentId, agentDescription);
        }

        return agentDescription;
    }

    private Date getDate(SolrDocument solrDocument, String fieldName) {
        return (Date) solrDocument.getFieldValue(fieldName);
    }

    private String getFirstFacetValue(QueryResponse response, String fieldName) {
        FacetField facetField = response.getFacetField(fieldName);

        if (facetField == null) {
            return null;
        }

        List<Count> values = facetField.getValues();
        if (values == null || values.isEmpty()) {
            return null;
        }

        return values.get(0).getName();
    }

    private String getString(SolrDocument solrDocument, String fieldName) {
        return (String) solrDocument.getFieldValue(fieldName);
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
                .getActiveSlicesMap(cloudSolrClient.getDefaultCollection());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        String sliceName = MessageFormat.format("{0,number,0000}-{1,number,00}", calendar.get(YEAR), calendar.get(MONTH) + 1);

        if (this.activeSlicesMap.containsKey(sliceName)) {
            return sliceName;
        }

        return this.fallbackShard;
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

    private AgentDescription loadAgentDescription(String globalAgentId) throws SolrServerException, IOException {
        SolrQuery query = new SolrQuery(ALL_DOCS_QUERY);
        query.setRows(0);

        query.setFilterQueries(FIELD_GLOBAL_AGENT_ID + ":\"" + globalAgentId + "\"");

        query.setFacetMinCount(1);
        query.addFacetField(FIELD_SPACE, FIELD_SYSTEM, FIELD_PROPERTY_ID);

        QueryResponse response = this.solrClient.query(query);

        AgentDescription result = new AgentDescription();
        result.setGlobalId(globalAgentId);
        result.setName(this.capabilitiesService.getAgentId(globalAgentId));
        result.setSpace(this.getFirstFacetValue(response, FIELD_SPACE));
        result.setSystem(this.getFirstFacetValue(response, FIELD_SYSTEM));

        List<PropertyDescription> propertyDescriptions = new ArrayList<>();
        FacetField facetField = response.getFacetField(FIELD_PROPERTY_ID);
        for (Count eachValue : facetField.getValues()) {
            propertyDescriptions.add(this.createPropertyDescription(eachValue.getName()));
        }
        Collections.sort(propertyDescriptions);
        result.setPropertyDescriptions(propertyDescriptions);

        return result;
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

    private InputStream serializeResponse(SolrParams params, QueryResponse response) throws IOException {
        LocalSolrQueryRequest solrQueryRequest = new LocalSolrQueryRequest(null, params);
        SolrQueryResponse solrQueryResponse = new SolrQueryResponse();
        solrQueryResponse.setAllValues(response.getResponse());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(baos, "UTF-8");
        this.jsonResponseWriter.write(writer, solrQueryRequest, solrQueryResponse);
        writer.flush();

        return new ByteArrayInputStream(baos.toByteArray());
    }

    protected class RefreshAgentDescriptionCacheTask extends TimerTask {

        @Override
        public void run() {
            SolrEventService.this.refreshAgentDescriptionCache();
        }
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
                this.eventStreamer.streamEvent(SolrEventService.this.createEvent(solrDocument));
            } catch (IOException e) {
                throw EventStoreException.retrieveFailed("Failed to stream events.", e);
            }
        }
    }

    private static class SolrNativeQueryResult implements NativeQueryResult {

        private final InputStream inputStream;

        public SolrNativeQueryResult(InputStream inputStream) {
            super();
            this.inputStream = new AutoCloseInputStream(inputStream);
        }

        @Override
        public String getContentType() {
            return "application/json;charset=UTF-8";
        }

        @Override
        public void writeTo(OutputStream outputStream) throws IOException {
            IOUtils.copy(this.inputStream, outputStream);
        }
    }
}
