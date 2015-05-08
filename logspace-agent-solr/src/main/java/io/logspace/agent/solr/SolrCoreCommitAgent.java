/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.solr;

import static io.logspace.agent.api.order.TriggerType.Event;
import static io.logspace.agent.api.order.TriggerType.Off;

import org.apache.solr.core.SolrCore;
import org.apache.solr.search.SolrIndexSearcher;

public class SolrCoreCommitAgent extends AbstractSolrCoreAgent {

    private SolrCoreCommitAgent(SolrCore core) {
        super(core, "/commit", Off, Event);

        this.getSolrCore().registerNewSearcherListener(this);
        this.getSolrCore().getUpdateHandler().registerCommitCallback(this);
        this.getSolrCore().getUpdateHandler().registerSoftCommitCallback(this);
    }

    public static void create(SolrCore solrCore) {
        new SolrCoreCommitAgent(solrCore);
    }

    @Override
    public void newSearcher(SolrIndexSearcher newSearcher, SolrIndexSearcher currentSearcher) {
        if (!this.isEnabled()) {
            return;
        }

        SolrEventBuilder solrEventBuilder = SolrEventBuilder.createNewSearcherBuilder(this.getId(), this.getSystem(),
                this.getCoreName());
        solrEventBuilder.setWarmuptime(getLong(newSearcher.getStatistics(), "warmupTime"));
        this.sendEvent(solrEventBuilder.toEvent());
    }

    @Override
    public void postCommit() {
        if (!this.isEnabled()) {
            return;
        }

        SolrEventBuilder solrEventBuilder = SolrEventBuilder.createCommitBuilder(this.getId(), this.getSystem(), this.getCoreName());
        this.sendEvent(solrEventBuilder.toEvent());
    }

    @Override
    public void postSoftCommit() {
        if (!this.isEnabled()) {
            return;
        }

        SolrEventBuilder solrEventBuilder = SolrEventBuilder.createSoftCommitBuilder(this.getId(), this.getSystem(),
                this.getCoreName());
        this.sendEvent(solrEventBuilder.toEvent());
    }
}
