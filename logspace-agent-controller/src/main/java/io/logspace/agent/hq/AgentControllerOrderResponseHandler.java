/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.hq;

import io.logspace.agent.api.json.AgentControllerOrdersJsonDeserializer;
import io.logspace.agent.api.order.AgentControllerOrder;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;

public class AgentControllerOrderResponseHandler implements ResponseHandler<AgentControllerOrder> {

    private static final int HTTP_3XX = 300;
    private static final int HTTP_NOT_MODIFIED = 304;

    private String lastModified;

    public String getLastModified() {
        return this.lastModified;
    }

    @Override
    public AgentControllerOrder handleResponse(HttpResponse response) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();

        if (statusLine.getStatusCode() == HTTP_NOT_MODIFIED) {
            return null;
        }

        if (statusLine.getStatusCode() >= HTTP_3XX) {
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }

        this.lastModified = response.getFirstHeader("Last-Modified").getValue();
        return AgentControllerOrdersJsonDeserializer.fromJson(entity.getContent());
    }
}