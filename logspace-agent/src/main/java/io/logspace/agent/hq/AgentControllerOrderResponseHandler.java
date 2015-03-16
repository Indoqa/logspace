/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.hq;

import io.logspace.agent.api.json.AgentControllerOrdersJsonSerializer;
import io.logspace.agent.api.order.AgentControllerOrder;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public class AgentControllerOrderResponseHandler implements ResponseHandler<AgentControllerOrder> {

    @Override
    public AgentControllerOrder handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();

        if (statusLine.getStatusCode() == 304) {
            EntityUtils.consume(entity);
            return null;
        }

        if (statusLine.getStatusCode() >= 300) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }

        InputStream inputStream = entity.getContent();
        try {
            return AgentControllerOrdersJsonSerializer.fromJson(inputStream);
        } finally {
            inputStream.close();
        }
    }
}