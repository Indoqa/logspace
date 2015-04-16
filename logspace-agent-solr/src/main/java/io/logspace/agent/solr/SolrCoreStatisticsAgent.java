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

    private SolrCoreStatisticsAgent(SolrCore core) {
        super(core, "/statistics", Off, Cron);
    }

    @SuppressWarnings("unused")
    public static void create(SolrCore solrCore) {
        new SolrCoreStatisticsAgent(solrCore);
    }

    private static long getIndexSize(NamedList<?> statistics) {
        Object indexSize = statistics.get("indexSize");

        if (indexSize instanceof String) {
            String indexSizeString = (String) indexSize;

            if (indexSizeString.endsWith(" GB")) {
                double value = parse(indexSizeString.substring(0, indexSizeString.length() - 3));
                return (long) (value * 1024 * 1024 * 1024);
            }

            if (indexSizeString.endsWith(" MB")) {
                double value = parse(indexSizeString.substring(0, indexSizeString.length() - 3));
                return (long) (value * 1024 * 1024);
            }

            if (indexSizeString.endsWith(" KB")) {
                double value = parse(indexSizeString.substring(0, indexSizeString.length() - 3));
                return (long) (value * 1024);
            }

            if (indexSizeString.endsWith(" bytes")) {
                double value = parse(indexSizeString.substring(0, indexSizeString.length() - 6));
                return (long) value;
            }
        }

        return 0;
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
        SolrEventBuilder solrEventBuilder = SolrEventBuilder.createStatisticsBuilder(this.getId(), this.getCoreName());

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

            solrEventBuilder.setFieldCacheSize(getLong(statistics, "size"));
            solrEventBuilder.setFieldCacheHitRatio(getFloat(statistics, "cumulative_hits"));
        }

        mBean = infoRegistry.get("fieldValueCache");
        if (mBean != null) {
            NamedList<?> statistics = mBean.getStatistics();

            solrEventBuilder.setFieldValueCacheSize(getLong(statistics, "size"));
            solrEventBuilder.setFieldValueCacheHitRatio(getFloat(statistics, "cumulative_hits"));
        }

        mBean = infoRegistry.get("queryCache");
        if (mBean != null) {
            NamedList<?> statistics = mBean.getStatistics();

            solrEventBuilder.setQueryCacheSize(getLong(statistics, "size"));
            solrEventBuilder.setQueryCacheHitRatio(getFloat(statistics, "cumulative_hits"));
        }

        mBean = infoRegistry.get("documentCache");
        if (mBean != null) {
            NamedList<?> statistics = mBean.getStatistics();

            solrEventBuilder.setDocumentCacheSize(getLong(statistics, "size"));
            solrEventBuilder.setDocumentCacheHitRatio(getFloat(statistics, "cumulative_hits"));
        }

        mBean = infoRegistry.get("filterCache");
        if (mBean != null) {
            NamedList<?> statistics = mBean.getStatistics();

            solrEventBuilder.setFilterCacheSize(getLong(statistics, "size"));
            solrEventBuilder.setFilterCacheHitRatio(getFloat(statistics, "cumulative_hits"));
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
}