/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.solr;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.indoqa.solr.server.factory.SolrServerFactory;

import io.logspace.agent.api.AgentControllerProvider;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.agent.test.TestAgentController;

public class SolrAgentTest {

    private static String getProperty(Collection<? extends EventProperty<String>> eventProperties, String name) {
        for (EventProperty<String> eachEventProperty : eventProperties) {
            if (eachEventProperty.getKey().equals(name)) {
                return eachEventProperty.getValue();
            }
        }

        return null;
    }

    @Test
    public void test() throws SolrServerException, IOException {
        TestAgentController.installIfRequired("./target/test-events.json");

        assertEquals(0, TestAgentController.getCollectedEventCount());

        SolrServerFactory solrServerFactory = new SolrServerFactory();
        solrServerFactory.setUrl("file:./target/solr");
        solrServerFactory.setEmbeddedSolrConfigurationDir("./src/test/resources/solr");
        solrServerFactory.initialize();

        SolrServer solrServer = solrServerFactory.getObject();

        assertEquals(0, TestAgentController.getCollectedEventCount());

        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.setField("id", UUID.randomUUID().toString());
        solrInputDocument.setField("timestamp", new Date());
        solrServer.add(solrInputDocument);

        assertEquals(0, TestAgentController.getCollectedEventCount());

        solrServer.commit(true, true);

        List<Event> collectedEvents = TestAgentController.getCollectedEvents();
        assertEquals(2, collectedEvents.size());
        Event event = collectedEvents.get(0);
        assertEquals("Embedded Core", getProperty(event.getStringProperties(), "core_name"));
        assertEquals("solr/core/commit", event.getType());

        event = collectedEvents.get(1);
        assertEquals("Embedded Core", getProperty(event.getStringProperties(), "core_name"));
        assertEquals("solr/core/new-searcher", event.getType());

        solrServerFactory.destroy();
        AgentControllerProvider.shutdown();
    }
}
