/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api;

import static io.logspace.agent.api.HttpStatusCode.InternalServerError;

public class DataRetrievalException extends AbstractLogspaceResourceException {

    private static final long serialVersionUID = 1L;

    public DataRetrievalException(String message, Throwable e) {
        super(message, InternalServerError, "DATA_RETRIEVAL_FAILED", e);

        this.setParameter("cause", e.getMessage());
    }
}
