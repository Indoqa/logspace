/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.system;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;
import java.util.function.IntFunction;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.junit.rules.ExternalResource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SolrClientResource extends ExternalResource {

    private static final int TIMEOUT = 3000;

    private static final JsonNodeResponseHandler JSON_NODE_RESPONSE_HANLDER = new JsonNodeResponseHandler();
    private CloseableHttpClient httpClient;

    public void assertSolrNumFound(String message, String baseUrl, IntFunction<Boolean> eval) {
        JsonNode rootNode;
        try {
            rootNode = this.httpClient.execute(new HttpGet(baseUrl + "/native-query?q=*:*&rows=0"), JSON_NODE_RESPONSE_HANLDER);
            int actualNumFound = rootNode.path("response").path("numFound").asInt();
            assertTrue(message + " Actual found " + actualNumFound + " event(s).", eval.apply(actualNumFound));
        } catch (IOException e) {
            fail("Error while accessing Solr: " + baseUrl);
        }
    }

    @Override
    protected void after() {
        try {
            this.httpClient.close();
        } catch (IOException e) {
            fail("Error while closing HTTP client: " + e.getMessage());
        }
    }

    @Override
    protected void before() throws Throwable {
        this.httpClient = HttpClients.custom()
            .disableAutomaticRetries()
            .setDefaultRequestConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT)
                .build())
            .setDefaultHeaders(Collections.singleton(new BasicHeader("Accept", APPLICATION_JSON.getMimeType())))
            .build();
    }

    private static class JsonNodeResponseHandler implements ResponseHandler<JsonNode> {

        private static final ObjectMapper MAPPER = new ObjectMapper();

        @Override
        public JsonNode handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            return MAPPER.readValue(response.getEntity().getContent(), JsonNode.class);
        }
    }
}
