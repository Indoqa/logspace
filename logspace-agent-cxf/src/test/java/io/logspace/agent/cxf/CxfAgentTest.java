/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.cxf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.agent.cxf.resource.TestCxfResource;
import io.logspace.agent.impl.AgentControllerProvider;
import io.logspace.agent.impl.TestAgentController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.transport.local.LocalConduit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CxfAgentTest {

    private final static String ENDPOINT_ADDRESS = "local://test-resource";
    private Server server;

    private static String getProperty(Collection<? extends EventProperty<String>> eventProperties, String name) {
        for (EventProperty<String> eachEventProperty : eventProperties) {
            if (eachEventProperty.getKey().equals(name)) {
                return eachEventProperty.getValue();
            }
        }

        return null;
    }

    public void executeCxfCrudMethods() {
        WebClient client = WebClient.create(ENDPOINT_ADDRESS);
        WebClient.getConfig(client).getRequestContext().put(LocalConduit.DIRECT_DISPATCH, Boolean.TRUE);

        client.accept("text/plain");
        client.path("/crud");
        String result = client.get(String.class);
        assertEquals("", result);

        result = client.post("my-value", String.class);
        assertEquals("my-value", result);

        result = client.get(String.class);
        assertEquals("my-value", result);

        client.put("my-value-updated");

        result = client.get(String.class);
        assertEquals("my-value-updated", result);

        client.delete();
        result = client.get(String.class);
        assertNull(result);
    }

    public void executeCxfGetMethod() {
        WebClient client = WebClient.create(ENDPOINT_ADDRESS);
        WebClient.getConfig(client).getRequestContext().put(LocalConduit.DIRECT_DISPATCH, Boolean.TRUE);

        client.accept("text/plain");
        client.path("/test");
        String result = client.get(String.class);

        assertEquals("test-response", result);
    }

    @Before
    public void startServer() {
        JAXRSServerFactoryBean serverFactory = new JAXRSServerFactoryBean();
        serverFactory.setResourceClasses(TestCxfResource.class);

        // add custom providers if any
        serverFactory.setProviders(new ArrayList<Object>());

        serverFactory.setResourceProvider(TestCxfResource.class, new SingletonResourceProvider(new TestCxfResource(), true));
        serverFactory.setAddress(ENDPOINT_ADDRESS);

        serverFactory.getInInterceptors().add(new CxfInAgent());
        serverFactory.getOutInterceptors().add(this.buildCxfOutAgent());

        this.server = serverFactory.create();
    }

    @After
    public void stopServer() {
        this.server.stop();
        this.server.destroy();
    }

    @Test
    public void test() {
        TestAgentController agentController = (TestAgentController) AgentControllerProvider.getAgentController();

        assertEquals(0, agentController.getCollectedEvents().size());

        this.executeCxfGetMethod();

        List<Event> collectedEvents = agentController.getCollectedEvents();
        assertEquals(1, collectedEvents.size());
        Event event = collectedEvents.get(0);
        assertEquals("GET", getProperty(event.getStringProperties(), "http_method"));
        assertEquals("/test", getProperty(event.getStringProperties(), "path"));

        this.executeCxfCrudMethods();

        collectedEvents = agentController.getCollectedEvents();
        assertEquals(8, collectedEvents.size());

        event = collectedEvents.get(1);
        assertEquals("GET", getProperty(event.getStringProperties(), "http_method"));
        assertEquals("/crud", getProperty(event.getStringProperties(), "path"));

        event = collectedEvents.get(6);
        assertEquals("DELETE", getProperty(event.getStringProperties(), "http_method"));
        assertEquals("/crud", getProperty(event.getStringProperties(), "path"));

        AgentControllerProvider.shutdown();
    }

    private CxfOutAgent buildCxfOutAgent() {
        TestAgentController.installIfRequired("./target/test-events.json");

        CxfOutAgent cxfOutAgent = new CxfOutAgent();

        cxfOutAgent.setAgentId("test-cxf");
        cxfOutAgent.initialize();

        return cxfOutAgent;
    }
}
