/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.hq;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.AgentControllerCapabilitiesJsonSerializer;
import io.logspace.agent.api.json.EventJsonSerializer;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.order.AgentControllerOrder;

import java.io.IOException;
import java.util.Collection;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HqClient {

    private CloseableHttpClient httpClient;

    private String baseUrl;
    private String agentControllerId;

    public HqClient(String baseUrl, String agentControllerId) {
        super();

        this.baseUrl = baseUrl;
        this.agentControllerId = agentControllerId;
        this.httpClient = HttpClients.createDefault();
    }

    private static StringEntity toJsonEntity(Collection<Event> event) throws IOException {
        return new StringEntity(EventJsonSerializer.toJson(event), APPLICATION_JSON);
    }

    private static StringEntity toJSonEntity(AgentControllerCapabilities capabilities) throws IOException {
        return new StringEntity(AgentControllerCapabilitiesJsonSerializer.toJson(capabilities), APPLICATION_JSON);
    }

    public void close() throws IOException {
        this.httpClient.close();
    }

    public AgentControllerOrder downloadOrder() throws IOException {
        HttpGet httpGet = new HttpGet(this.baseUrl + "/orders/" + this.agentControllerId);

        return this.httpClient.execute(httpGet, new AgentControllerOrderResponseHandler());
    }

    public void uploadCapabilities(AgentControllerCapabilities capabilities) throws IOException {
        HttpPut httpPut = new HttpPut(this.baseUrl + "/capabilities/" + this.agentControllerId);
        httpPut.setEntity(toJSonEntity(capabilities));

        this.httpClient.execute(httpPut, new UploadCapabilitiesResponseHandler());
    }

    public void uploadEvents(Collection<Event> events) throws IOException {
        HttpPost httpMethod = new HttpPost(this.baseUrl + "/events");
        httpMethod.setEntity(toJsonEntity(events));

        this.httpClient.execute(httpMethod, new UploadEventsResponseHandler());
    }
}