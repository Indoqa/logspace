/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.hq;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import io.logspace.agent.api.AgentControllerInitializationException;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.json.AgentControllerCapabilitiesJsonSerializer;
import io.logspace.agent.api.json.EventJsonSerializer;
import io.logspace.agent.api.order.AgentControllerCapabilities;
import io.logspace.agent.api.order.AgentControllerOrder;

import java.io.IOException;
import java.util.Collection;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HqClient {

    private static final int TIMEOUT = 3000;

    private CloseableHttpClient httpClient;

    private String baseUrl;
    private String agentControllerId;
    private String spaceToken;

    private final AgentControllerOrderResponseHandler agentControllerOrderResponseHandler = new AgentControllerOrderResponseHandler();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public HqClient(String baseUrl, String agentControllerId, String spaceToken) {
        super();

        this.baseUrl = baseUrl;
        if (this.baseUrl == null || this.baseUrl.trim().length() == 0) {
            throw new AgentControllerInitializationException("The base URL must not be empty!");
        }

        this.agentControllerId = agentControllerId;
        this.spaceToken = spaceToken;

        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(TIMEOUT).setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT).build();
        this.httpClient = HttpClients.custom().disableAutomaticRetries().setDefaultRequestConfig(requestConfig).build();
    }

    private static StringEntity toJsonEntity(Collection<Event> event) throws IOException {
        return new StringEntity(EventJsonSerializer.toJson(event), APPLICATION_JSON);
    }

    private static StringEntity toJSonEntity(AgentControllerCapabilities capabilities) throws IOException {
        return new StringEntity(AgentControllerCapabilitiesJsonSerializer.toJson(capabilities), APPLICATION_JSON);
    }

    public void close() throws IOException {
        this.logger.info("Closing now.");
        this.httpClient.close();
    }

    public AgentControllerOrder downloadOrder() throws IOException {
        HttpGet httpGet = new HttpGet(this.baseUrl + "/orders/" + this.agentControllerId);
        httpGet.addHeader("If-Modified-Since", this.agentControllerOrderResponseHandler.getLastModified());
        httpGet.addHeader("logspace.space-token", this.spaceToken);

        return this.httpClient.execute(httpGet, this.agentControllerOrderResponseHandler);
    }

    public void uploadCapabilities(AgentControllerCapabilities capabilities) throws IOException {
        HttpPut httpPut = new HttpPut(this.baseUrl + "/capabilities/" + this.agentControllerId);
        httpPut.setEntity(toJSonEntity(capabilities));
        httpPut.addHeader("logspace.space-token", this.spaceToken);

        this.httpClient.execute(httpPut, new UploadCapabilitiesResponseHandler());
    }

    public void uploadEvents(Collection<Event> events) throws IOException {
        this.logger.info("Uploading {} event(s) using space-token '{}'.", events.size(), this.spaceToken);

        HttpPost httpPost = new HttpPost(this.baseUrl + "/events");
        httpPost.setEntity(toJsonEntity(events));
        httpPost.addHeader("logspace.space-token", this.spaceToken);

        this.httpClient.execute(httpPost, new UploadEventsResponseHandler());
    }
}