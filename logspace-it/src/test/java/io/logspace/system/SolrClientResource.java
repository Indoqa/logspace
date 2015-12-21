package io.logspace.system;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.function.IntFunction;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
        this.httpClient = HttpClients
            .custom()
            .disableAutomaticRetries()
            .setDefaultRequestConfig(RequestConfig
                .custom()
                .setConnectionRequestTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT)
                .build())
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
