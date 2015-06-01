/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.cxf;

import static io.logspace.agent.cxf.CxfMessageHelper.getProtocolHeaderValue;
import io.logspace.agent.api.LogspaceHttpHeaders;
import io.logspace.agent.api.event.context.DefaultEventContext;
import io.logspace.agent.api.event.context.EventContext;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class CxfInAgent extends AbstractPhaseInterceptor<Message> {

    public static final String CXF_AGENT_DURATION_KEY = "io.logspace.agent.cxf.duration";
    public static final String CXF_AGENT_EVENT_CONTEXT = "io.logspace.agent.cxf.event.context";

    private EventContext eventContext;

    public CxfInAgent() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(Message message) {
        message.getExchange().put(CXF_AGENT_EVENT_CONTEXT, this.extractEventContext(message));

        message.getExchange().put(CXF_AGENT_DURATION_KEY, System.nanoTime());
    }

    public void setEventContext(EventContext eventContext) {
        this.eventContext = eventContext;
    }

    private EventContext extractEventContext(Message message) {
        if (this.eventContext != null) {
            return this.eventContext;
        }

        String globalId = getProtocolHeaderValue(message, LogspaceHttpHeaders.GLOBAL_ID_HEADER);
        String parentId = getProtocolHeaderValue(message, LogspaceHttpHeaders.PARENT_ID_HEADER);

        return new DefaultEventContext(globalId, parentId);
    }
}
