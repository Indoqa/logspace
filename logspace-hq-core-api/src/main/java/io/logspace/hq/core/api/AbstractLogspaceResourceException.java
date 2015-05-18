/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractLogspaceResourceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int responseCode;
    private final ErrorData errorData;

    public AbstractLogspaceResourceException(String message, int responseCode, String type) {
        super(message);

        this.responseCode = responseCode;
        this.errorData = ErrorData.create(type, message);
    }

    public AbstractLogspaceResourceException(String message, int responseCode, String type, Throwable cause) {
        super(message, cause);

        this.responseCode = responseCode;
        this.errorData = ErrorData.create(type, message);
    }

    public ErrorData getErrorData() {
        return this.errorData;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void setParameter(String name, Object value) {
        this.errorData.setParameter(name, value);
    }

    public static class ErrorData {

        private String type;
        private String message;

        private Map<String, Object> parameters = new HashMap<>();

        public static ErrorData create(String type, String message) {
            ErrorData result = new ErrorData();

            result.setType(type);
            result.setMessage(message);

            return result;
        }

        public String getMessage() {
            return this.message;
        }

        public Map<String, Object> getParameters() {
            return this.parameters;
        }

        public String getType() {
            return this.type;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setParameter(String name, Object value) {
            this.parameters.put(name, value);
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}