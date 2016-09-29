/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AbstractSolrService {

    @Inject
    @Qualifier("logspace-solr-client")
    protected SolrClient solrClient;

    protected boolean isCloud;

    @PostConstruct
    public void initialize() {
        this.isCloud = this.solrClient instanceof CloudSolrClient;

        if (this.isCloud) {
            ((CloudSolrClient) this.solrClient).connect();
        }
    }
}