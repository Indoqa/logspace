/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.solr;

import static io.logspace.agent.solr.SolrEventBuilder.getLong;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrEventListener;
import org.apache.solr.search.SolrIndexSearcher;

import io.logspace.agent.api.AbstractApplicationAgent;

public class SolrCoreCommitAgent extends AbstractApplicationAgent implements SolrEventListener {

    private SolrCore solrCore;

    public SolrCoreCommitAgent(SolrCore solrCore) {
        super(solrCore.getName() + "/commit", "solr/core/commit");

        this.solrCore = solrCore;
        SolrCore.log.info("Initializing " + this.getClass().getSimpleName() + " for Core '" + this.solrCore + "'.");

        this.solrCore.registerNewSearcherListener(this);
        this.solrCore.getUpdateHandler().registerCommitCallback(this);
        this.solrCore.getUpdateHandler().registerSoftCommitCallback(this);
    }

    @Override
    public void init(@SuppressWarnings("rawtypes") NamedList args) {
        // do nothing
    }

    @Override
    public void newSearcher(SolrIndexSearcher newSearcher, SolrIndexSearcher currentSearcher) {
        if (!this.isEnabled()) {
            return;
        }

        SolrEventBuilder solrEventBuilder = SolrEventBuilder.createNewSearcherBuilder(this.getId(), this.getSystem(), this.getMarker(),
            this.getCoreName());
        solrEventBuilder.setWarmuptime(getLong(newSearcher.getStatistics(), "warmupTime"));
        this.sendEvent(solrEventBuilder.toEvent());
    }

    @Override
    public void postCommit() {
        if (!this.isEnabled()) {
            return;
        }

        SolrEventBuilder solrEventBuilder = SolrEventBuilder.createCommitBuilder(this.getId(), this.getSystem(), this.getMarker(),
            this.getCoreName());
        this.sendEvent(solrEventBuilder.toEvent());
    }

    @Override
    public void postSoftCommit() {
        if (!this.isEnabled()) {
            return;
        }

        SolrEventBuilder solrEventBuilder = SolrEventBuilder.createSoftCommitBuilder(this.getId(), this.getSystem(), this.getMarker(),
            this.getCoreName());
        this.sendEvent(solrEventBuilder.toEvent());
    }

    private String getCoreName() {
        return this.solrCore.getName();
    }
}
