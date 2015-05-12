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
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static org.apache.solr.common.params.ShardParams._ROUTE_;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.agent.api.order.PropertyDescription;
import io.logspace.agent.api.order.PropertyDescription.PropertyType;
import io.logspace.hq.core.api.AgentDescription;
import io.logspace.hq.core.api.CapabilitiesService;
import io.logspace.hq.core.api.DataDefinition;
import io.logspace.hq.core.api.DateRange;
import io.logspace.hq.core.api.EventService;
import io.logspace.hq.core.api.Suggestion;
import io.logspace.hq.core.api.SuggestionInput;
import io.logspace.hq.solr.FacetBuilder.Facet;
import io.logspace.hq.solr.FacetBuilder.QueryFacet;
import io.logspace.hq.solr.FacetBuilder.StatisticFacet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@Named
public class SolrEventService implements EventService {

    private static final String FACETS_NAME = "facets";
    private static final String VALUE_FACET_NAME = "value";
    private static final String FIELD_TOKENIZED_SEARCH_FIELD = "tokenized_search_field";
    private static final String FIELD_SPACE = "space";
    private static final String FIELD_SYSTEM = "system";
    private static final String FIELD_AGENT_ID = "agent_id";
    private static final String FIELD_GLOBAL_AGENT_ID = "global_agent_id";

    private static final String FIELD_TYPE = "type";
    private static final String FIELD_GLOBAL_ID = "global_id";
    private static final String FIELD_PARENT_ID = "parent_id";
    private static final String FIELD_TIMESTAMP = "timestamp";

    private static final String FIELD_PROPERTY_ID = "property_id";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private SolrClient solrClient;
    // private SolrServer solrServer;

    @Inject
    private CapabilitiesService capabilitiesService;

    @Value("${logspace.solr.fallback-shard}")
    private String fallbackShard;

    private boolean isCloud;
    private Map<String, Slice> activeSlicesMap;
    private long nextSliceUpdate;

    @Override
    @SuppressWarnings("unchecked")
    public Object[] getData(DataDefinition dataDefinition) {
        SolrQuery solrQuery = new SolrQuery("*:*");

        solrQuery.addFilterQuery(FIELD_GLOBAL_AGENT_ID + ":" + escapeSolr(dataDefinition.getGlobalAgentId()));
        solrQuery.addFilterQuery(this.getTimestampRangeQuery(dataDefinition.getDateRange()));
        solrQuery.set("json.facet", this.createJsonFacets(dataDefinition));

        try {
            QueryResponse response = this.solrClient.query(solrQuery, METHOD.POST);

            NamedList<Object> facets = (NamedList<Object>) response.getResponse().get(FACETS_NAME);
            Object[] result = new Object[facets.size() - 1];

            for (int i = 1; i < facets.size(); i++) {
                int index = Integer.parseInt(facets.getName(i));
                Object value = ((NamedList<?>) facets.getVal(i)).get(VALUE_FACET_NAME);

                result[index] = value;
            }

            return result;
        } catch (SolrServerException | IOException e) {
            // TODO: property exception class
            throw new RuntimeException("Could not extract data.");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Suggestion getSuggestion(SuggestionInput input) {
        Suggestion result = new Suggestion();

        SolrQuery solrQuery = new SolrQuery("*:*");

        solrQuery.setRows(0);
        solrQuery
                .set("json.facet",
                        "{space-facet:{terms:{field:space,facet:{system-facet:{terms:{field:system,facet:{global_agent_id-facet:{terms:{field:global_agent_id,facet:{property_id-facet:{terms:{field:property_id}}}}}}}}}}}}");

        if (!StringUtils.isBlank(input.getText())) {
            solrQuery.addFilterQuery(FIELD_TOKENIZED_SEARCH_FIELD + ":" + escapeSolr(input.getText()) + "*");
        }

        this.addFilterQuery(solrQuery, FIELD_PROPERTY_ID, input.getPropertyId());
        this.addFilterQuery(solrQuery, FIELD_SPACE, input.getSpace());
        this.addFilterQuery(solrQuery, FIELD_SYSTEM, input.getSystem());

        try {
            QueryResponse response = this.solrClient.query(solrQuery);

            NamedList<Object> facets = (NamedList<Object>) response.getResponse().get(FACETS_NAME);

            List<NamedList<?>> spaceBuckets = this.getSubFacetBuckets(facets, "space-facet");
            for (NamedList<?> eachSpaceBucket : spaceBuckets) {
                String space = this.getValue(eachSpaceBucket);

                List<NamedList<?>> systemBuckets = this.getSubFacetBuckets(eachSpaceBucket, "system-facet");
                for (NamedList<?> eachSystemBucket : systemBuckets) {
                    String system = this.getValue(eachSystemBucket);

                    List<NamedList<?>> globalAgentIdBuckets = this.getSubFacetBuckets(eachSystemBucket, "global_agent_id-facet");
                    for (NamedList<?> eachGlobalAgentIdBucket : globalAgentIdBuckets) {
                        String globalAgentId = this.getValue(eachGlobalAgentIdBucket);

                        AgentDescription agentDescription = this.capabilitiesService.getAgentDescription(globalAgentId);

                        if (agentDescription == null) {
                            agentDescription = new AgentDescription();
                            agentDescription.setGlobalId(globalAgentId);
                            agentDescription.setName(this.capabilitiesService.getAgentId(globalAgentId));
                            agentDescription.setSystem(system);
                            agentDescription.setSpace(space);

                            result.addAgentDescription(agentDescription);

                            List<NamedList<?>> propertyIdBuckets = this.getSubFacetBuckets(eachGlobalAgentIdBucket,
                                    "property_id-facet");
                            for (NamedList<?> eachPropertyIdBucket : propertyIdBuckets) {
                                String propertyId = this.getValue(eachPropertyIdBucket);
                                agentDescription.addPropertyDescription(this.createPropertyDescription(propertyId));
                            }
                        }

                        result.addAgentDescription(agentDescription);
                    }
                }
            }

            return result;
        } catch (SolrServerException | IOException e) {
            // TODO: proper exception class
            throw new RuntimeException("Failed to create suggestions", e);
        }
    }

    @PostConstruct
    public void initialize() {
        this.isCloud = this.solrClient instanceof CloudSolrClient;

        if (this.isCloud) {
            ((CloudSolrClient) this.solrClient).connect();
        }
    }

    @Override
    public void store(Collection<? extends Event> events, String space) {
        if (events == null || events.isEmpty()) {
            return;
        }

        this.logger.info("Storing {} event(s).", events.size());

        try {
            Collection<SolrInputDocument> inputDocuments = this.createInputDocuments(events, space);
            this.solrClient.add(inputDocuments);
        } catch (SolrServerException | IOException e) {
            this.logger.error("Failed to store events.", e);
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

    private SolrInputDocument createInputDocument(Event event, String space) {
        SolrInputDocument result = new SolrInputDocument();

        result.addField("id", event.getId());

        result.addField(FIELD_GLOBAL_AGENT_ID, this.capabilitiesService.getGlobalAgentId(space, event.getSystem(), event.getAgentId()));
        result.addField(FIELD_SPACE, space);
        result.addField(FIELD_SYSTEM, event.getSystem());
        result.addField(FIELD_AGENT_ID, event.getAgentId());

        result.addField(FIELD_TYPE, event.getType().orElse(null));
        result.addField(FIELD_TIMESTAMP, event.getTimestamp());
        result.addField(FIELD_PARENT_ID, event.getParentEventId().orElse(null));
        result.addField(FIELD_GLOBAL_ID, event.getGlobalEventId().orElse(null));

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

    private String createJsonFacets(DataDefinition dataDefinition) {
        FacetBuilder facetBuilder = new FacetBuilder();

        Facet valueFacet = StatisticFacet.with(VALUE_FACET_NAME, dataDefinition.getFacetFunction());

        Date startDate = dataDefinition.getDateRange().getStart();
        Date endDate = dataDefinition.getDateRange().getEnd();
        int gap = dataDefinition.getDateRange().getGap();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate)) {
            String name1 = String.valueOf(facetBuilder.getFacetCount());

            Date start = calendar.getTime();
            calendar.add(Calendar.SECOND, gap);
            Date end = calendar.getTime();
            String query = this.getTimestampRangeQuery(start, end);

            facetBuilder.addFacet(QueryFacet.with(name1, query, valueFacet));
        }

        return facetBuilder.toJson();
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

    @SuppressWarnings("unchecked")
    private List<NamedList<?>> getSubFacetBuckets(NamedList<?> namedList, String facetName) {
        if (namedList == null) {
            return Collections.emptyList();
        }

        NamedList<?> facet = (NamedList<?>) namedList.get(facetName);
        if (facet == null) {
            return Collections.emptyList();
        }

        return (List<NamedList<?>>) facet.get("buckets");
    }

    private String getTargetShard(Date timestamp) {
        if (!this.isCloud) {
            return null;
        }

        CloudSolrClient cloudSolrClient = (CloudSolrClient) this.solrClient;

        if (System.currentTimeMillis() > this.nextSliceUpdate) {
            this.nextSliceUpdate = System.currentTimeMillis() + 1000;
            this.activeSlicesMap = cloudSolrClient.getZkStateReader().getClusterState()
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
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    private String getTimestampRangeQuery(DateRange dateRange) {
        return this.getTimestampRangeQuery(dateRange.getStart(), dateRange.getEnd());
    }

    private String getValue(NamedList<?> bucket) {
        return (String) bucket.get("val");
    }
}
