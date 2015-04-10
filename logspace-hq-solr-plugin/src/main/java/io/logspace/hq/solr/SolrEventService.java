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
    public void store(Collection<? extends Event> events, String space) {
        if (events == null || events.isEmpty()) {
            return;
        }

        this.logger.info("Storing {} event(s).", events.size());

        Collection<SolrInputDocument> inputDocuments = this.createInputDocuments(events, space);

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

    private SolrInputDocument createInputDocument(Event event, String space) {
        SolrInputDocument result = new SolrInputDocument();

        result.addField("id", event.getId());
        result.addField("type", event.getType().orElse(null));
        result.addField("timestamp", event.getTimestamp());
        result.addField("parent_id", event.getParentEventId().orElse(null));
        result.addField("global_id", event.getGlobalEventId().orElse(null));

        result.addField("space", space);

        this.addProperties(result, event.getBooleanProperties(), "boolean_property_");
        this.addProperties(result, event.getDateProperties(), "date_property_");
        this.addProperties(result, event.getDoubleProperties(), "double_property_");
        this.addProperties(result, event.getFloatProperties(), "float_property_");
        this.addProperties(result, event.getIntegerProperties(), "integer_property_");
        this.addProperties(result, event.getLongProperties(), "long_property_");
        this.addProperties(result, event.getStringProperties(), "string_property_");

        return result;
    }

    private Collection<SolrInputDocument> createInputDocuments(Collection<? extends Event> events, String space) {
        Collection<SolrInputDocument> result = new ArrayList<SolrInputDocument>();

        for (Event eachEvent : events) {
            result.add(this.createInputDocument(eachEvent, space));
        }

        return result;
    }
}
