/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.hq.core.api.EventService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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
    public void store(Collection<? extends Event> events) {
        if (events == null || events.isEmpty()) {
            return;
        }

        this.logger.info("Storing {} event(s).", events.size());

        Collection<SolrInputDocument> inputDocuments = new ArrayList<SolrInputDocument>();

        for (Event eachEvent : events) {
            SolrInputDocument document = new SolrInputDocument();

            document.addField("id", eachEvent.getId());
            document.addField("type", eachEvent.getType().orElse(null));
            document.addField("timestamp", eachEvent.getTimestamp());
            document.addField("parent_id", eachEvent.getParentEventId().orElse(null));
            document.addField("global_id", eachEvent.getGlobalEventId().orElse(null));

            if (eachEvent.hasProperties()) {
                for (EventProperty eachProperty : eachEvent.getProperties()) {
                    document.addField("property_" + eachEvent.getId(), eachProperty.getValue());
                }
            }

            inputDocuments.add(document);
        }

        try {
            this.solrServer.add(inputDocuments);
            this.solrServer.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }
}
