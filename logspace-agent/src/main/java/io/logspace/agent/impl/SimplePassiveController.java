package io.logspace.agent.impl;

import io.logspace.agent.api.Agent;
import io.logspace.agent.api.AgentController;
import io.logspace.agent.api.event.Event;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class SimplePassiveController implements AgentController {

    private Collection<Agent> receivers = Collections.synchronizedSet(new HashSet<Agent>());
    private CloseableHttpClient httpclient;
    private String baseUrl;

    public SimplePassiveController(String baseUrl) {
        this.baseUrl = baseUrl;
        this.initialize();
    }

    private static StringEntity toJsonEntity(Collection<Event> event) throws IOException {
        return new StringEntity(EventJsonSerializer.toJson(event), ContentType.APPLICATION_JSON);
    }

    @Override
    public void register(Agent agent) {
        this.receivers.add(agent);
    }

    @Override
    public void send(Collection<Event> events) {
        try {
            this.sendEvents(events);
        } catch (IOException e) {

        }
    }

    @Override
    public void send(Event event) {
        this.send(Collections.singleton(event));
    }

    private void initialize() {
        this.httpclient = HttpClients.createDefault();
    }

    private void sendEvents(Collection<Event> event) throws IOException, ClientProtocolException {
        HttpPut httpPut = new HttpPut(this.baseUrl + "/events/");
        httpPut.setEntity(toJsonEntity(event));

        ResponseHandler<Void> responseHandler = new ResponseHandler<Void>() {

            @Override
            public Void handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                return null;
            }

        };

        this.httpclient.execute(httpPut, responseHandler);
    }
}
