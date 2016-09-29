/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import static com.indoqa.lang.util.StringUtils.escapeSolr;
import static io.logspace.hq.solr.EventFieldConstants.*;
import static io.logspace.hq.solr.SolrQueryHelper.*;
import static java.util.concurrent.TimeUnit.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;

import com.indoqa.lang.util.TimeTracker;
import com.indoqa.solr.facet.api.*;

import io.logspace.agent.api.order.PropertyDescription;
import io.logspace.hq.core.api.agent.AgentService;
import io.logspace.hq.core.api.capabilities.CapabilitiesService;
import io.logspace.hq.rest.api.DataRetrievalException;
import io.logspace.hq.rest.api.agentactivity.AgentActivities;
import io.logspace.hq.rest.api.agentactivity.AgentActivity;
import io.logspace.hq.rest.api.suggestion.AgentDescription;
import io.logspace.hq.rest.api.suggestion.Suggestion;
import io.logspace.hq.rest.api.suggestion.SuggestionInput;

@Named
public class SolrAgentService extends AbstractSolrService implements AgentService {

    private static final long AGENT_DESCRIPTION_REFRESH_INTERVAL = 60000L;

    @Inject
    private CapabilitiesService capabilitiesService;

    private final Map<String, AgentDescription> cachedAgentDescriptions = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public AgentActivities getAgentActivities(int start, int count, int durationSeconds, int steps, String sort) {
        SolrQuery solrQuery = new SolrQuery(ALL_DOCS_QUERY);
        solrQuery.setRows(0);

        Date endDate = new Date();
        Date startDate = new Date(endDate.getTime() - SECONDS.toMillis(durationSeconds));

        solrQuery.addFilterQuery(getTimestampRangeQuery(startDate, endDate));

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
    public Set<String> getEventPropertyNames(String... globalAgentIds) {
        SolrQuery solrQuery = new SolrQuery(ALL_DOCS_QUERY);
        solrQuery.setRows(0);

        StringBuilder globalAgentIdFilterQuery = new StringBuilder();
        globalAgentIdFilterQuery.append(FIELD_GLOBAL_AGENT_ID);
        globalAgentIdFilterQuery.append(":(");
        for (String eachGlobalAgentId : globalAgentIds) {
            globalAgentIdFilterQuery.append(escapeSolr(eachGlobalAgentId));
            globalAgentIdFilterQuery.append(" OR ");
        }
        globalAgentIdFilterQuery.setLength(globalAgentIdFilterQuery.length() - 4);
        globalAgentIdFilterQuery.append(')');
        solrQuery.addFilterQuery(globalAgentIdFilterQuery.toString());

        solrQuery.setFacet(true);
        solrQuery.setFacetMinCount(1);
        solrQuery.addFacetField(FIELD_PROPERTY_ID);

        try {
            QueryResponse response = this.solrClient.query(solrQuery);

            Set<String> result = new TreeSet<>();
            for (Count eachCount : response.getFacetField(FIELD_PROPERTY_ID).getValues()) {
                result.add(eachCount.getName());
            }

            return result;
        } catch (SolrException | SolrServerException | IOException e) {
            throw new DataRetrievalException("Failed to retrieve event property names", e);
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

        addFilterQuery(solrQuery, FIELD_PROPERTY_ID, input.getPropertyId());
        addFilterQuery(solrQuery, FIELD_SPACE, input.getSpaceId());
        addFilterQuery(solrQuery, FIELD_SYSTEM, input.getSystemId());

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
    @PostConstruct
    public void initialize() {
        new Timer(true)
            .schedule(new RefreshAgentDescriptionCacheTask(), AGENT_DESCRIPTION_REFRESH_INTERVAL, AGENT_DESCRIPTION_REFRESH_INTERVAL);
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
            propertyDescriptions.add(createPropertyDescription(eachValue.getName()));
        }
        Collections.sort(propertyDescriptions);
        result.setPropertyDescriptions(propertyDescriptions);

        return result;
    }

    protected class RefreshAgentDescriptionCacheTask extends TimerTask {

        @Override
        public void run() {
            SolrAgentService.this.refreshAgentDescriptionCache();
        }
    }
}
