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

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class SolrEventService implements EventService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private SolrServer solrServer;

    @Override
    public void store(Collection<Event> events) {
        SolrInputDocument doc = new SolrInputDocument();
        String id = UUID.randomUUID().toString();
        doc.addField("id", id);
        doc.addField("name", "name: " + id);
        doc.addField("text", "text: " + id);
        try {
            this.solrServer.add(doc);
            this.solrServer.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        this.logger.info("SolrServer=" + this.solrServer);
    }
}
