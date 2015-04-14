/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.cxf;

import io.logspace.agent.api.AbstractAgent;
import io.logspace.agent.api.Agent;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.order.AgentCapabilities;
import io.logspace.agent.api.order.AgentOrder;
import io.logspace.agent.api.order.TriggerType;
import io.logspace.agent.impl.AgentControllerProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequestWrapper;
import javax.ws.rs.core.HttpHeaders;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class CxfOutAgent extends AbstractPhaseInterceptor<Message> implements Agent {

    private DelegateAgent delegateAgent;

    private String agentId;

    public CxfOutAgent() {
        super(Phase.PRE_STREAM);
    }

    @Override
    public void execute(AgentOrder agentOrder) {
        this.delegateAgent.execute(agentOrder);
    }

    @Override
    public AgentCapabilities getCapabilities() {
        return this.delegateAgent.getCapabilities();
    }

    @Override
    public String getType() {
        return this.delegateAgent.getType();
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        this.logEvent(message);
    }

    @PostConstruct
    public void initialize() {
        this.delegateAgent = new DelegateAgent(this.agentId);
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    private Object getProtocolHeaderValue(Message message, String header) {
        @SuppressWarnings("unchecked")
        Map<String, List<?>> headers = (Map<String, List<?>>) message.get(Message.PROTOCOL_HEADERS);
        if (headers == null || !headers.containsKey(header)) {
            return null;
        }

        return headers.get(header).get(0);
    }

    private void logDuration(CxfEventBuilder cxfEventBuilder, Message message) {
        Long executionStartTime = (Long) message.getExchange().remove(CxfInAgent.CXF_AGENT_DURATION_KEY);
        cxfEventBuilder.setDuration(TimeUnit.MILLISECONDS.convert(System.nanoTime() - executionStartTime, TimeUnit.NANOSECONDS));
    }

    private void logEndpointUrl(CxfEventBuilder cxfEventBuilder, String endpointAddress) {
        try {
            URL endpointUrl = new URL(endpointAddress);

            cxfEventBuilder.setProtocol(endpointUrl.getProtocol());
            if (endpointUrl.getPort() != -1) {
                cxfEventBuilder.setPort(endpointUrl.getPort());
            }
            cxfEventBuilder.setDomain(endpointUrl.getHost());
        } catch (MalformedURLException e) {
            // nothing we can do
        }
    }

    private void logEvent(Message message) {
        if (!this.delegateAgent.isAgentEnabled()) {
            return;
        }
        CxfEventBuilder cxfEventBuilder = new CxfEventBuilder(this.delegateAgent.getId());

        this.logInMessageUrl(cxfEventBuilder, message);
        this.logOutMessage(cxfEventBuilder, message);
        this.logDuration(cxfEventBuilder, message);

        this.delegateAgent.sendLogspaceEvent(cxfEventBuilder.toEvent());
    }

    private void logInMessageUrl(CxfEventBuilder cxfEventBuilder, Message message) {
        if (message.getExchange() == null || message.getExchange().getInMessage() == null) {
            return;
        }
        Message inMessage = message.getExchange().getInMessage();

        cxfEventBuilder.setHttpMethod((String) inMessage.get(Message.HTTP_REQUEST_METHOD));
        cxfEventBuilder.setQueryString((String) inMessage.get(Message.QUERY_STRING));
        cxfEventBuilder.setPath((String) inMessage.get(Message.REQUEST_URI));

        cxfEventBuilder.setRequestId((String) this.getProtocolHeaderValue(inMessage, "Request-ID"));

        this.logIpAddress(cxfEventBuilder, inMessage);

        String endpointAddress = HttpUtils.getEndpointAddress(inMessage);
        this.logEndpointUrl(cxfEventBuilder, endpointAddress);
    }

    private void logIpAddress(CxfEventBuilder cxfEventBuilder, Message message) {
        Object request = message.get("HTTP.REQUEST");
        if (request instanceof ServletRequestWrapper) {
            cxfEventBuilder.setIpAddress(((ServletRequestWrapper) request).getRemoteAddr());
        }
    }

    private void logOutMessage(CxfEventBuilder cxfEventBuilder, Message message) {
        if (message.getExchange() == null || message.getExchange().getOutMessage() == null) {
            return;
        }

        Message outMessage = message.getExchange().getOutMessage();

        cxfEventBuilder.setResponseCode((Integer) outMessage.get(Message.RESPONSE_CODE));
        cxfEventBuilder.setLocation((String) this.getProtocolHeaderValue(outMessage, HttpHeaders.LOCATION));
    }

    private class DelegateAgent extends AbstractAgent {

        public DelegateAgent(String id) {
            super();

            this.updateCapabilities(TriggerType.Event, TriggerType.Off);
            this.setId(id);
            this.setType("CXF");

            this.setAgentController(AgentControllerProvider.getAgentController());
        }

        protected boolean isAgentEnabled() {
            return this.isEnabled();
        }

        protected void sendLogspaceEvent(Event event) {
            this.sendEvent(event);
        }
    }
}
