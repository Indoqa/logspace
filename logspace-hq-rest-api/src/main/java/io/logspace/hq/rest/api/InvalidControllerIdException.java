/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api;

import static io.logspace.agent.api.HttpStatusCode.BadRequest;

public class InvalidControllerIdException extends AbstractLogspaceResourceException {

    private static final long serialVersionUID = 1L;

    public InvalidControllerIdException(String message) {
        super(message, BadRequest, "INVALID_CONTROLLER_ID");
    }
}
