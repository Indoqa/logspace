/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.solr;

import io.logspace.agent.api.AbstractAgent;
import io.logspace.agent.api.order.TriggerType;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrEventListener;
import org.apache.solr.search.SolrIndexSearcher;

public abstract class AbstractSolrCoreAgent extends AbstractAgent implements SolrEventListener {

    private final SolrCore solrCore;

    protected AbstractSolrCoreAgent(SolrCore solrCore, String type, TriggerType... triggerType) {
        super(solrCore.getName() + type, "solr/core" + type, triggerType);

        SolrCore.log.info("Initializing " + this.getClass().getSimpleName() + " for Core '" + solrCore + "'.");
        this.solrCore = solrCore;
    }

    protected static boolean getBoolean(NamedList<?> namedList, String name) {
        Object value = namedList.get(name);

        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }

        return false;
    }

    protected static double getDouble(NamedList<?> namedList, String name) {
        Object value = namedList.get(name);

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        return 0;
    }

    protected static float getFloat(NamedList<?> namedList, String name) {
        Object value = namedList.get(name);

        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }

        return 0;
    }

    protected static int getInt(NamedList<?> namedList, String name) {
        Object value = namedList.get(name);

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        return 0;
    }

    protected static long getLong(NamedList<?> namedList, String name) {
        Object value = namedList.get(name);

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        return 0;
    }

    @Override
    public void init(@SuppressWarnings("rawtypes") NamedList args) {
        // do nothing
    }

    @Override
    public void newSearcher(SolrIndexSearcher newSearcher, SolrIndexSearcher currentSearcher) {
        // do nothing
    }

    @Override
    public void postCommit() {
        // do nothing
    }

    @Override
    public void postSoftCommit() {
        // do nothing
    }

    protected String getCoreName() {
        return this.solrCore.getName();
    }

    protected SolrCore getSolrCore() {
        return this.solrCore;
    }
}