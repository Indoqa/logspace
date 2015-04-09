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

        Collection<SolrInputDocument> inputDocuments = this.createInputDocuments(events);

        try {
            this.solrServer.add(inputDocuments);
        } catch (SolrServerException | IOException e) {
            this.logger.error("Failed to store events.", e);
        }
    }

    private void addProperties(SolrInputDocument document, Iterable<? extends EventProperty<?>> properties, String prefix) {
        for (EventProperty<?> eachProperty : properties) {
            document.addField(prefix + eachProperty.getKey(), eachProperty.getValue());
        }
    }

    private SolrInputDocument createInputDocument(Event eachEvent) {
        SolrInputDocument result = new SolrInputDocument();

        result.addField("id", eachEvent.getId());
        result.addField("type", eachEvent.getType().orElse(null));
        result.addField("timestamp", eachEvent.getTimestamp());
        result.addField("parent_id", eachEvent.getParentEventId().orElse(null));
        result.addField("global_id", eachEvent.getGlobalEventId().orElse(null));

        this.addProperties(result, eachEvent.getBooleanProperties(), "boolean_property_");
        this.addProperties(result, eachEvent.getDateProperties(), "date_property_");
        this.addProperties(result, eachEvent.getDoubleProperties(), "double_property_");
        this.addProperties(result, eachEvent.getFloatProperties(), "float_property_");
        this.addProperties(result, eachEvent.getIntegerProperties(), "integer_property_");
        this.addProperties(result, eachEvent.getLongProperties(), "long_property_");
        this.addProperties(result, eachEvent.getStringProperties(), "string_property_");

        return result;
    }

    private Collection<SolrInputDocument> createInputDocuments(Collection<? extends Event> events) {
        Collection<SolrInputDocument> result = new ArrayList<SolrInputDocument>();

        for (Event eachEvent : events) {
            result.add(this.createInputDocument(eachEvent));
        }

        return result;
    }
}
