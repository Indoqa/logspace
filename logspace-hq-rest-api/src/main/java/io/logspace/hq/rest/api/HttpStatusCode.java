/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api;

public enum HttpStatusCode {

    Accepted(202, true), NotModified(304, false), BadRequest(400, true), Forbidden(403, true), NotFound(404, true),
    InternalServerError(500, true);

    private final int code;
    private final boolean hasBody;

    private HttpStatusCode(int code, boolean hasBody) {
        this.code = code;
        this.hasBody = hasBody;
    }

    public int getCode() {
        return this.code;
    }

    public boolean hasBody() {
        return this.hasBody;
    }

    public boolean matches(int statusCode) {
        return this.code == statusCode;
    }

    public boolean notMatches(int statusCode) {
        return !this.matches(statusCode);
    }
}
