/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSolrEventService {

    @Inject
    @EventQualifier
    protected SolrClient solrClient;

    protected boolean isCloud;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void initialize() {
        this.isCloud = this.solrClient instanceof CloudSolrClient;

        if (this.isCloud) {
            ((CloudSolrClient) this.solrClient).connect();
        }
    }
}
