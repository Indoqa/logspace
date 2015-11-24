/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api;

import static io.logspace.hq.rest.api.HttpStatusCode.InternalServerError;

public class EventStoreException extends AbstractLogspaceResourceException {

    private static final long serialVersionUID = 1L;

    private EventStoreException(String message, String type, Throwable cause) {
        super(message, InternalServerError, type);

        this.setParameter("cause", cause.getMessage());
    }

    public static EventStoreException retrieveFailed(String message, Throwable cause) {
        return new EventStoreException(message, "EVENT_RETRIEVAL_FAILED", cause);
    }

    public static EventStoreException storeFailed(String message, Throwable cause) {
        return new EventStoreException(message, "EVENT_STORAGE_FAILED", cause);
    }

    public static EventStoreException streamFailed(String message, Throwable cause) {
        return new EventStoreException(message, "EVENT_STREAM_FAILED", cause);
    }
}
