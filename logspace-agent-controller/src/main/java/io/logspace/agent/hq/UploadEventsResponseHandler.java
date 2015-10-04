/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.hq;

import static io.logspace.agent.api.HttpStatusCode.Accepted;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

public class UploadEventsResponseHandler implements ResponseHandler<Void> {

    @Override
    public Void handleResponse(final HttpResponse response) throws IOException {
        if (Accepted.notMatches(response.getStatusLine().getStatusCode())) {
            throw new UploadException();
        }

        return null;
    }
}
