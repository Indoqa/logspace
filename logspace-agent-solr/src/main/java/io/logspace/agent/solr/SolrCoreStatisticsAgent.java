/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.solr;

import static io.logspace.agent.api.order.TriggerType.Cron;
import static io.logspace.agent.api.order.TriggerType.Off;
import io.logspace.agent.api.order.AgentOrder;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrInfoMBean;

public class SolrCoreStatisticsAgent extends AbstractSolrCoreAgent {

    private static final int INDEX_SIZE_UNIT_FACTOR = 1024;
    private static final String FIELD_SIZE = "size";
    private static final String FIELD_CUMULATIVE_HITS = "cumulative_hits";

    public SolrCoreStatisticsAgent(SolrCore core) {
        super(core, "/statistics", Off, Cron);
    }

    private static long getIndexSize(NamedList<?> statistics) {
        Object indexSize = statistics.get("indexSize");

        if (!(indexSize instanceof String)) {
            return 0;
        }

        String indexSizeString = (String) indexSize;
        long factor = 1;
        double value = 0;

        for (IndexUnit eachIndexUnit : IndexUnit.values()) {
            if (indexSizeString.endsWith(eachIndexUnit.name())) {
                value = parse(indexSizeString.substring(0, indexSizeString.length() - eachIndexUnit.name().length()));
                break;
            }

            factor *= INDEX_SIZE_UNIT_FACTOR;
        }

        return (long) (factor * value);
    }

    private static double parse(String value) {
        try {
            NumberFormat formatter = NumberFormat.getNumberInstance(Locale.ROOT);
            return formatter.parse(value).doubleValue();
        } catch (ParseException e) {
            SolrCore.log.error("Failed to parse numeric value '{}'", value);
            return 0;
        }
    }

    @Override
    public void execute(AgentOrder agentOrder) {
        SolrEventBuilder solrEventBuilder = SolrEventBuilder.createStatisticsBuilder(this.getId(), this.getSystem(),
                this.getCoreName());

        Map<String, SolrInfoMBean> infoRegistry = this.getSolrCore().getInfoRegistry();
        this.addIndexStatistics(solrEventBuilder, infoRegistry);
        this.addUpdateStatistics(solrEventBuilder, infoRegistry);
        this.addReplicationStatistics(solrEventBuilder, infoRegistry);
        this.addCacheStatistics(solrEventBuilder, infoRegistry);

        this.sendEvent(solrEventBuilder.toEvent());
    }

    private void addCacheStatistics(SolrEventBuilder solrEventBuilder, Map<String, SolrInfoMBean> infoRegistry) {
        SolrInfoMBean mBean = infoRegistry.get("fieldCache");
        if (mBean != null) {
            NamedList<?> statistics = mBean.getStatistics();

            solrEventBuilder.setFieldCacheSize(getLong(statistics, FIELD_SIZE));
            solrEventBuilder.setFieldCacheHitRatio(getFloat(statistics, FIELD_CUMULATIVE_HITS));
        }

        mBean = infoRegistry.get("fieldValueCache");
        if (mBean != null) {
            NamedList<?> statistics = mBean.getStatistics();

            solrEventBuilder.setFieldValueCacheSize(getLong(statistics, FIELD_SIZE));
            solrEventBuilder.setFieldValueCacheHitRatio(getFloat(statistics, FIELD_CUMULATIVE_HITS));
        }

        mBean = infoRegistry.get("queryCache");
        if (mBean != null) {
            NamedList<?> statistics = mBean.getStatistics();

            solrEventBuilder.setQueryCacheSize(getLong(statistics, FIELD_SIZE));
            solrEventBuilder.setQueryCacheHitRatio(getFloat(statistics, FIELD_CUMULATIVE_HITS));
        }

        mBean = infoRegistry.get("documentCache");
        if (mBean != null) {
            NamedList<?> statistics = mBean.getStatistics();

            solrEventBuilder.setDocumentCacheSize(getLong(statistics, FIELD_SIZE));
            solrEventBuilder.setDocumentCacheHitRatio(getFloat(statistics, FIELD_CUMULATIVE_HITS));
        }

        mBean = infoRegistry.get("filterCache");
        if (mBean != null) {
            NamedList<?> statistics = mBean.getStatistics();

            solrEventBuilder.setFilterCacheSize(getLong(statistics, FIELD_SIZE));
            solrEventBuilder.setFilterCacheHitRatio(getFloat(statistics, FIELD_CUMULATIVE_HITS));
        }
    }

    private void addIndexStatistics(SolrEventBuilder solrEventBuilder, Map<String, SolrInfoMBean> infoRegistry) {
        SolrInfoMBean mBean = infoRegistry.get("/select");
        if (mBean != null) {
            NamedList<?> statistics = mBean.getStatistics();

            solrEventBuilder.setRequestCount(getLong(statistics, "requests"));
            solrEventBuilder.setAverageRequestTime(getDouble(statistics, "avgTimePerRequest"));
            solrEventBuilder.setAverageRequestsPerSecond(getDouble(statistics, "avgRequestsPerSecond"));
            solrEventBuilder.set75thPercentileRequestTime(getDouble(statistics, "75thPcRequestTime"));
            solrEventBuilder.set95thPercentileRequestTime(getDouble(statistics, "95thPcRequestTime"));
            solrEventBuilder.set99thPercentileRequestTime(getDouble(statistics, "99thPcRequestTime"));
            solrEventBuilder.set999thPercentileRequestTime(getDouble(statistics, "999thPcRequestTime"));
        }

        mBean = infoRegistry.get("searcher");
        if (mBean != null) {
            NamedList<?> statistics = mBean.getStatistics();

            solrEventBuilder.setDocumentCount(getInt(statistics, "numDocs"));
        }
    }

    private void addReplicationStatistics(SolrEventBuilder solrEventBuilder, Map<String, SolrInfoMBean> infoRegistry) {
        SolrInfoMBean mBean = infoRegistry.get("/replication");
        if (mBean == null) {
            return;
        }

        NamedList<?> statistics = mBean.getStatistics();

        solrEventBuilder.setIndexSize(getIndexSize(statistics));
        solrEventBuilder.setGeneration(getLong(statistics, "generation"));
        solrEventBuilder.setIndexVersion(getLong(statistics, "indexVersion"));
        solrEventBuilder.setIsMaster(getBoolean(statistics, "isMaster"));
        solrEventBuilder.setIsSlave(getBoolean(statistics, "isSlave"));
    }

    private void addUpdateStatistics(SolrEventBuilder solrEventBuilder, Map<String, SolrInfoMBean> infoRegistry) {
        SolrInfoMBean mBean = infoRegistry.get("updateHandler");
        if (mBean == null) {
            return;
        }

        NamedList<?> statistics = mBean.getStatistics();

        solrEventBuilder.setUpdates(getLong(statistics, "cumulative_adds"));
        solrEventBuilder.setDeletes(getLong(statistics, "cumulative_deletesByQuery") + getLong(statistics, "cumulative_deletesById"));
        solrEventBuilder.setCommits(getLong(statistics, "commits"));
    }

    private static enum IndexUnit {

        bytes, KB, MB, GB;
    }
}
