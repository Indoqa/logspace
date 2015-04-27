/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import static com.indoqa.commons.lang.util.StringUtils.escapeSolr;
import static java.text.MessageFormat.format;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static org.apache.solr.common.params.ShardParams._ROUTE_;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.hq.core.api.DataDefinition;
import io.logspace.hq.core.api.EventService;
import io.logspace.hq.core.api.Suggestion;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.StreamingResponseCallback;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.Slice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@Named
public class SolrEventService implements EventService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private SolrServer solrServer;

    @Value("${logspace.solr.fallback-shard}")
    private String fallbackShard;

    private boolean isCloud;

    @Override
    public Object[] getData(DataDefinition dataDefinition) {
        Class<?> propertyType = this.getPropertyType(dataDefinition);
        if (propertyType == null) {
            // TODO: property exception class
            throw new RuntimeException("Unsupported property type.");
        }

        SolrQuery solrQuery = new SolrQuery("*:*");
        solrQuery.addFilterQuery("space:" + escapeSolr(dataDefinition.getSpace()));
        solrQuery.addFilterQuery("agent_id:" + escapeSolr(dataDefinition.getAgentId()));
        solrQuery.addFilterQuery(dataDefinition.getPropertyId() + ":*");
        solrQuery.setFields("timestamp", dataDefinition.getPropertyId());
        solrQuery.setRows(Integer.MAX_VALUE);

        try {
            PropertyValueCollector propertyValueCollector = new PropertyValueCollector(dataDefinition);
            this.solrServer.queryAndStreamResponse(solrQuery, propertyValueCollector);
            return propertyValueCollector.getData();
        } catch (SolrServerException | IOException e) {
            // TODO: proper exception class
            throw new RuntimeException("Failed to retrieve data", e);
        }
    }

    @Override
    public Suggestion getSuggestion(String input) {
        Suggestion result = new Suggestion();

        SolrQuery solrQuery = new SolrQuery("*:*");
        solrQuery.setRows(0);

        solrQuery.addFilterQuery(format("tokenized_search_field:{0}*", escapeSolr(input)));

        solrQuery.addFacetField("space");
        solrQuery.addFacetField("agent_id");
        solrQuery.addFacetField("property_name");
        solrQuery.setFacetLimit(1000);
        solrQuery.setFacetMinCount(1);

        try {
            QueryResponse response = this.solrServer.query(solrQuery);

            String lowercaseInput = StringUtils.lowerCase(input);
            FacetField spaceFacetField = response.getFacetField("space");
            if (spaceFacetField != null) {
                result.setSpaces(this.getNames(spaceFacetField.getValues()));
            }

            FacetField agentIdFacetField = response.getFacetField("agent_id");
            if (agentIdFacetField != null) {
                result.setAgentIds(this.getNames(agentIdFacetField.getValues()));
            }

            FacetField propertyNameFacetField = response.getFacetField("property_name");
            if (propertyNameFacetField != null) {
                result.setPropertyNames(this.getNamesWithInput(propertyNameFacetField.getValues(), lowercaseInput));
            }

            return result;
        } catch (SolrServerException e) {
            // TODO: proper exception class
            throw new RuntimeException("Failed to create suggestions", e);
        }
    }

    @PostConstruct
    public void initialize() {
        this.isCloud = this.solrServer instanceof CloudSolrServer;
    }

    @Override
    public void store(Collection<? extends Event> events, String space) {
        if (events == null || events.isEmpty()) {
            return;
        }

        this.logger.info("Storing {} event(s).", events.size());

        Collection<SolrInputDocument> inputDocuments = this.createInputDocuments(events, space);

        try {
            this.solrServer.add(inputDocuments);
        } catch (SolrServerException | IOException e) {
            this.logger.error("Failed to store events.", e);
        }
    }

    private void addProperties(SolrInputDocument document, Iterable<? extends EventProperty<?>> properties, String prefix) {
        for (EventProperty<?> eachProperty : properties) {
            document.addField(prefix + eachProperty.getKey(), eachProperty.getValue());
            document.addField("property_name", eachProperty.getKey());
        }
    }

    private SolrInputDocument createInputDocument(Event event, String space) {
        SolrInputDocument result = new SolrInputDocument();

        result.addField("id", event.getId());
        result.addField("agent_id", event.getAgentId());
        result.addField("type", event.getType().orElse(null));
        result.addField("timestamp", event.getTimestamp());
        result.addField("parent_id", event.getParentEventId().orElse(null));
        result.addField("global_id", event.getGlobalEventId().orElse(null));

        result.addField("space", space);

        if (this.isCloud) {
            result.setField(_ROUTE_, this.getTargetShard(event.getTimestamp()));
        }

        this.addProperties(result, event.getBooleanProperties(), "boolean_property_");
        this.addProperties(result, event.getDateProperties(), "date_property_");
        this.addProperties(result, event.getDoubleProperties(), "double_property_");
        this.addProperties(result, event.getFloatProperties(), "float_property_");
        this.addProperties(result, event.getIntegerProperties(), "integer_property_");
        this.addProperties(result, event.getLongProperties(), "long_property_");
        this.addProperties(result, event.getStringProperties(), "string_property_");

        return result;
    }

    private Collection<SolrInputDocument> createInputDocuments(Collection<? extends Event> events, String space) {
        Collection<SolrInputDocument> result = new ArrayList<SolrInputDocument>();

        for (Event eachEvent : events) {
            result.add(this.createInputDocument(eachEvent, space));
        }

        return result;
    }

    private List<String> getNames(List<Count> values) {
        List<String> result = new ArrayList<>();

        for (Count eachValue : values) {
            result.add(eachValue.getName());
        }

        return result;
    }

    private List<String> getNamesWithInput(List<Count> values, String lowercaseInput) {
        List<String> result = new ArrayList<>();

        for (Count eachValue : values) {
            if (eachValue.getName().toLowerCase().contains(lowercaseInput)) {
                result.add(eachValue.getName());
            }
        }

        // if we didn't find a single matching value we take them all, apparently the input does not apply here
        if (result.isEmpty()) {
            for (Count eachValue : values) {
                result.add(eachValue.getName());
            }
        }

        return result;
    }

    private Class<?> getPropertyType(DataDefinition dataDefinition) {
        if (dataDefinition.getPropertyId().startsWith("long_property")) {
            return Long.class;
        }

        if (dataDefinition.getPropertyId().startsWith("int_property")) {
            return Integer.class;
        }

        if (dataDefinition.getPropertyId().startsWith("float_property")) {
            return Float.class;
        }

        if (dataDefinition.getPropertyId().startsWith("double_property")) {
            return Double.class;
        }

        return null;
    }

    private String getTargetShard(Date timestamp) {
        if (!this.isCloud) {
            return null;
        }

        CloudSolrServer cloudSolrServer = (CloudSolrServer) this.solrServer;
        Map<String, Slice> activeSlicesMap = cloudSolrServer.getZkStateReader().getClusterState()
                .getActiveSlicesMap(cloudSolrServer.getDefaultCollection());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        String sliceName = MessageFormat.format("{0,number,0000}-{1,number,00}", calendar.get(YEAR), calendar.get(MONTH));

        if (activeSlicesMap.containsKey(sliceName)) {
            return sliceName;
        }

        return "fallback";
    }

    private static class AverageBucket<T extends Number> extends SumBucket<T> {

        private int count;

        @Override
        public void add(T value) {
            super.add(value);
            this.count++;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T getAggregate() {
            T value = super.getAggregate();

            if (value instanceof Integer) {
                return (T) Integer.valueOf(value.intValue() / this.count);
            }

            if (value instanceof Long) {
                return (T) Long.valueOf(value.longValue() / this.count);
            }

            if (value instanceof Float) {
                return (T) Float.valueOf(value.floatValue() / this.count);
            }

            if (value instanceof Double) {
                return (T) Double.valueOf(value.doubleValue() / this.count);
            }

            return null;
        }
    }

    private static interface Bucket<T> {

        void add(T value);

        T getAggregate();
    }

    private static class CountBucket implements Bucket<Object> {

        private int count;

        @Override
        public void add(Object value) {
            this.count++;
        }

        @Override
        public Integer getAggregate() {
            return this.count;
        }
    }

    private static class MaxBucket<T extends Comparable<T>> implements Bucket<T> {

        private T max;

        @Override
        public void add(T value) {
            if (this.max == null || value.compareTo(this.max) > 0) {
                this.max = value;
            }
        }

        @Override
        public T getAggregate() {
            return this.max;
        }
    }

    private static class MinBucket<T extends Comparable<T>> implements Bucket<T> {

        private T min;

        @Override
        public void add(T value) {
            if (this.min == null || value.compareTo(this.min) < 0) {
                this.min = value;
            }
        }

        @Override
        public T getAggregate() {
            return this.min;
        }
    }

    private class PropertyValueCollector extends StreamingResponseCallback {

        private final Map<Integer, Bucket<?>> data = new ConcurrentHashMap<Integer, Bucket<?>>();
        private final DataDefinition dataDefinition;

        public PropertyValueCollector(DataDefinition dataDefinition) {
            this.dataDefinition = dataDefinition;
        }

        public Object[] getData() {
            long intervalLength = this.dataDefinition.getDateRange().getEnd().getTime()
                    - this.dataDefinition.getDateRange().getStart().getTime();
            int bucketCount = (int) ((intervalLength + this.dataDefinition.getDateRange().getGap() - 1) / this.dataDefinition
                    .getDateRange().getGap());

            Object[] result = new Object[bucketCount];

            for (int i = 0; i < bucketCount; i++) {
                Bucket<?> bucket = this.data.get(i);
                if (bucket == null) {
                    continue;
                }

                result[i] = bucket.getAggregate();
            }

            return result;
        }

        @Override
        public void streamDocListInfo(long numFound, long start, Float maxScore) {
            // do nothing
        }

        @Override
        @SuppressWarnings("unchecked")
        public void streamSolrDocument(SolrDocument doc) {
            Object propertyValue = doc.getFirstValue(this.dataDefinition.getPropertyId());
            if (propertyValue == null) {
                return;
            }

            int bucketIndex = this.getBucketIndex((Date) doc.getFirstValue("timestamp"));

            Bucket<Object> bucket = (Bucket<Object>) this.data.get(bucketIndex);
            if (bucket == null) {
                bucket = this.createBucket();
                this.data.put(bucketIndex, bucket);
            }
            bucket.add(propertyValue);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private Bucket<Object> createBucket() {
            switch (this.dataDefinition.getAggregate()) {
                case average:
                    return new AverageBucket();
                    // break;

                case count:
                    return new CountBucket();
                    // break;

                case max:
                    return new MaxBucket();
                    // break;

                case min:
                    return new MinBucket();
                    // break;

                case sum:
                    return new SumBucket();
                    // break;

                default:
                    break;
            }

            return null;
        }

        private int getBucketIndex(Date date) {
            Date start = this.dataDefinition.getDateRange().getStart();
            long offset = date.getTime() - start.getTime();
            return (int) (offset / this.dataDefinition.getDateRange().getGap());
        }
    }

    private static class SumBucket<T extends Number> implements Bucket<T> {

        @SuppressWarnings("unchecked")
        private T sum = (T) Integer.valueOf(0);

        @Override
        @SuppressWarnings("unchecked")
        public void add(T value) {
            if (value instanceof Integer) {
                this.sum = (T) Integer.valueOf(value.intValue() + this.sum.intValue());
            }

            if (value instanceof Long) {
                this.sum = (T) Long.valueOf(value.longValue() + this.sum.longValue());
            }

            if (value instanceof Float) {
                this.sum = (T) Float.valueOf(value.floatValue() + this.sum.floatValue());
            }

            if (value instanceof Double) {
                this.sum = (T) Double.valueOf(value.doubleValue() + this.sum.doubleValue());
            }
        }

        @Override
        public T getAggregate() {
            return this.sum;
        }
    }
}
