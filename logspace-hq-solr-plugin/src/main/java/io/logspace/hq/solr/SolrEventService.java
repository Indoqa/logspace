/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import io.logspace.agent.api.event.Event;
import io.logspace.hq.core.api.EventService;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.solr.client.solrj.SolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class SolrEventService implements EventService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private SolrServer solrServer;

    @Override
    public void store(Collection<Event> events) {
        this.logger.info("SolrServer=" + this.solrServer);
    }
}
