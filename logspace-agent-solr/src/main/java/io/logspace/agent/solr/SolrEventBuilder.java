/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.solr;

import io.logspace.agent.api.event.AbstractEventBuilder;
import io.logspace.agent.api.event.Optional;

public final class SolrEventBuilder extends AbstractEventBuilder {

    public static final String PROPERTY_CORE_NAME = "core_name";
    public static final String PROPERTY_DOCUMENT_COUNT = "document_count";
    public static final String PROPERTY_REQUEST_COUNT = "request_count";
    public static final String PROPERTY_AVERAGE_REQUEST_TIME = "average_request_time";
    public static final String PROPERTY_AVERAGE_REQUESTS_PER_SECOND = "average_requests_per_second";
    public static final String PROPERTY_75TH_PERCENTILE_REQUEST_TIME = "75th_percentile_request_time";
    public static final String PROPERTY_95TH_PERCENTILE_REQUEST_TIME = "95th_percentile_request_time";
    public static final String PROPERTY_99TH_PERCENTILE_REQUEST_TIME = "99th_percentile_request_time";
    public static final String PROPERTY_999TH_PERCENTILE_REQUEST_TIME = "999th_percentile_request_time";
    public static final String PROPERTY_UPDATES = "updates";
    public static final String PROPERTY_DELETES = "deletes";
    public static final String PROPERTY_COMMITS = "commits";
    public static final String PROPERTY_INDEX_SIZE = "index_size";
    public static final String PROPERTY_IS_MASTER = "is_master";
    public static final String PROPERTY_IS_SLAVE = "is_slave";
    public static final String PROPERTY_GENERATION = "generation";
    public static final String PROPERTY_INDEX_VERSION = "index_version";
    public static final String PROPERTY_WARMUP_TIME = "warmup_time";

    public static final String PROPERTY_FIELD_CACHE_HIT_RATIO = "field_cache_hit_ratio";
    public static final String PROPERTY_FIELD_CACHE_SIZE = "field_cache_size";
    public static final String PROPERTY_FIELD_VALUE_CACHE_HIT_RATIO = "field_value_cache_hit_ratio";
    public static final String PROPERTY_FIELD_VALUE_CACHE_SIZE = "field_value_cache_size";
    public static final String PROPERTY_QUERY_CACHE_HIT_RATIO = "query_cache_hit_ratio";
    public static final String PROPERTY_QUERY_CACHE_SIZE = "query_cache_size";
    public static final String PROPERTY_DOCUMENT_CACHE_HIT_RATIO = "document_cache_hit_ratio";
    public static final String PROPERTY_DOCUMENT_CACHE_SIZE = "document_cache_size";
    public static final String PROPERTY_FILTER_CACHE_HIT_RATIO = "filter_cache_hit_ratio";
    public static final String PROPERTY_FILTER_CACHE_SIZE = "filter_cache_size";

    private static final Optional<String> STATISTICS_EVENT_TYPE = Optional.of("solr/core/statistics");
    private static final Optional<String> COMMIT_EVENT_TYPE = Optional.of("solr/core/commit");
    private static final Optional<String> SOFT_COMMIT_EVENT_TYPE = Optional.of("solr/core/soft-commit");
    private static final Optional<String> NEW_SEARCHER_EVENT_TYPE = Optional.of("solr/core/new-searcher");

    private Optional<String> eventType;

    private SolrEventBuilder(String agentId, String system, Optional<String> eventType, String coreName) {
        super(agentId, system);

        this.eventType = eventType;
        this.addProperty(PROPERTY_CORE_NAME, coreName);
    }

    public static SolrEventBuilder createCommitBuilder(String agentId, String system, String coreName) {
        return new SolrEventBuilder(agentId, system, COMMIT_EVENT_TYPE, coreName);
    }

    public static SolrEventBuilder createNewSearcherBuilder(String agentId, String system, String coreName) {
        return new SolrEventBuilder(agentId, system, NEW_SEARCHER_EVENT_TYPE, coreName);
    }

    public static SolrEventBuilder createSoftCommitBuilder(String agentId, String system, String coreName) {
        return new SolrEventBuilder(agentId, system, SOFT_COMMIT_EVENT_TYPE, coreName);
    }

    public static SolrEventBuilder createStatisticsBuilder(String agentId, String system, String coreName) {
        return new SolrEventBuilder(agentId, system, STATISTICS_EVENT_TYPE, coreName);
    }

    public void set75thPercentileRequestTime(double requestTime) {
        this.addProperty(PROPERTY_75TH_PERCENTILE_REQUEST_TIME, requestTime);
    }

    public void set95thPercentileRequestTime(double requestTime) {
        this.addProperty(PROPERTY_95TH_PERCENTILE_REQUEST_TIME, requestTime);
    }

    public void set999thPercentileRequestTime(double requestTime) {
        this.addProperty(PROPERTY_999TH_PERCENTILE_REQUEST_TIME, requestTime);
    }

    public void set99thPercentileRequestTime(double requestTime) {
        this.addProperty(PROPERTY_99TH_PERCENTILE_REQUEST_TIME, requestTime);
    }

    public void setAverageRequestsPerSecond(double averageRequestPerSecond) {
        this.addProperty(PROPERTY_AVERAGE_REQUESTS_PER_SECOND, averageRequestPerSecond);
    }

    public void setAverageRequestTime(double averageRequestTime) {
        this.addProperty(PROPERTY_AVERAGE_REQUEST_TIME, averageRequestTime);
    }

    public void setCommits(long commits) {
        this.addProperty(PROPERTY_COMMITS, commits);
    }

    public void setDeletes(long deletes) {
        this.addProperty(PROPERTY_DELETES, deletes);
    }

    public void setDocumentCacheHitRatio(float hitRatio) {
        this.addProperty(PROPERTY_DOCUMENT_CACHE_HIT_RATIO, hitRatio);
    }

    public void setDocumentCacheSize(long size) {
        this.addProperty(PROPERTY_DOCUMENT_CACHE_SIZE, size);
    }

    public void setDocumentCount(int documentCount) {
        this.addProperty(PROPERTY_DOCUMENT_COUNT, documentCount);
    }

    public void setFieldCacheHitRatio(float hitRatio) {
        this.addProperty(PROPERTY_FIELD_CACHE_HIT_RATIO, hitRatio);
    }

    public void setFieldCacheSize(long size) {
        this.addProperty(PROPERTY_FIELD_CACHE_SIZE, size);
    }

    public void setFieldValueCacheHitRatio(float hitRatio) {
        this.addProperty(PROPERTY_FIELD_VALUE_CACHE_HIT_RATIO, hitRatio);
    }

    public void setFieldValueCacheSize(long size) {
        this.addProperty(PROPERTY_FIELD_VALUE_CACHE_SIZE, size);
    }

    public void setFilterCacheHitRatio(float hitRatio) {
        this.addProperty(PROPERTY_FILTER_CACHE_HIT_RATIO, hitRatio);
    }

    public void setFilterCacheSize(long size) {
        this.addProperty(PROPERTY_FILTER_CACHE_SIZE, size);
    }

    public void setGeneration(long generation) {
        this.addProperty(PROPERTY_GENERATION, generation);
    }

    public void setIndexSize(long indexSize) {
        this.addProperty(PROPERTY_INDEX_SIZE, indexSize);
    }

    public void setIndexVersion(long indexVersion) {
        this.addProperty(PROPERTY_INDEX_VERSION, indexVersion);
    }

    public void setIsMaster(boolean isMaster) {
        this.addProperty(PROPERTY_IS_MASTER, isMaster);
    }

    public void setIsSlave(boolean isSlave) {
        this.addProperty(PROPERTY_IS_SLAVE, isSlave);
    }

    public void setQueryCacheHitRatio(float hitRatio) {
        this.addProperty(PROPERTY_QUERY_CACHE_HIT_RATIO, hitRatio);
    }

    public void setQueryCacheSize(long size) {
        this.addProperty(PROPERTY_QUERY_CACHE_SIZE, size);
    }

    public void setRequestCount(long requestCount) {
        this.addProperty(PROPERTY_REQUEST_COUNT, requestCount);
    }

    public void setUpdates(long updates) {
        this.addProperty(PROPERTY_UPDATES, updates);
    }

    public void setWarmuptime(long warmupTime) {
        this.addProperty(PROPERTY_WARMUP_TIME, warmupTime);
    }

    @Override
    protected Optional<String> getType() {
        return this.eventType;
    }
}