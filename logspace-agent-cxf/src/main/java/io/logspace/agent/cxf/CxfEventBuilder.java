/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.cxf;

import io.logspace.agent.api.event.AbstractEventBuilder;

public class CxfEventBuilder extends AbstractEventBuilder {

    public static final String TYPE = "cxf";

    public static final String PROPERTY_PROTOCOL = "protocol";
    public static final String PROPERTY_DOMAIN = "domain";
    public static final String PROPERTY_PORT = "port";
    public static final String PROPERTY_PATH = "path";
    public static final String PROPERTY_QUERY_STRING = "query_string";
    public static final String PROPERTY_RESPONSE_CODE = "response_code";
    public static final String PROPERTY_LOCATION = "location";
    public static final String PROPERTY_IP_ADDRESS = "ip_address";
    public static final String PROPERTY_HTTP_METHOD = "http_method";
    public static final String PROPERTY_DURATION = "duration";
    public static final String PROPERTY_REQUEST_ID = "request_id";
    public static final String PROPERTY_GLOBAL_REQUEST_ID = "global_request_id";
    public static final String PROPERTY_PARENT_REQUEST_ID = "parent_request_id";

    public CxfEventBuilder(String agentId, String system, String marker) {
        super(agentId, system, marker);
    }

    public CxfEventBuilder setDomain(String domain) {
        if (domain == null) {
            return this;
        }

        this.addProperty(PROPERTY_DOMAIN, domain);
        return this;
    }

    public CxfEventBuilder setDuration(Long duration) {
        if (duration == null) {
            return this;
        }

        this.addProperty(PROPERTY_DURATION, duration);
        return this;
    }

    public CxfEventBuilder setGlobalRequestId(String globalRequestId) {
        if (globalRequestId == null) {
            return this;
        }
        this.addProperty(PROPERTY_GLOBAL_REQUEST_ID, globalRequestId);
        return this;
    }

    public CxfEventBuilder setHttpMethod(String httpMethod) {
        if (httpMethod == null) {
            return this;
        }

        this.addProperty(PROPERTY_HTTP_METHOD, httpMethod);
        return this;
    }

    public CxfEventBuilder setIpAddress(String ipAddress) {
        if (ipAddress == null) {
            return this;
        }

        this.addProperty(PROPERTY_IP_ADDRESS, ipAddress);
        return this;
    }

    public CxfEventBuilder setLocation(String location) {
        if (location == null) {
            return this;
        }
        this.addProperty(PROPERTY_LOCATION, location);
        return this;
    }

    public CxfEventBuilder setParentRequestId(String parentRequestId) {
        if (parentRequestId == null) {
            return this;
        }
        this.addProperty(PROPERTY_PARENT_REQUEST_ID, parentRequestId);
        return this;
    }

    public CxfEventBuilder setPath(String path) {
        if (path == null) {
            return this;
        }
        this.addProperty(PROPERTY_PATH, path);
        return this;
    }

    public CxfEventBuilder setPort(Integer port) {
        if (port == null) {
            return this;
        }
        this.addProperty(PROPERTY_PORT, port);
        return this;
    }

    public CxfEventBuilder setProtocol(String protocol) {
        if (protocol == null) {
            return this;
        }

        this.addProperty(PROPERTY_PROTOCOL, protocol);
        return this;
    }

    public CxfEventBuilder setQueryString(String queryString) {
        if (queryString == null) {
            return this;
        }
        this.addProperty(PROPERTY_QUERY_STRING, queryString);
        return this;
    }

    public CxfEventBuilder setRequestId(String requestId) {
        if (requestId == null) {
            return this;
        }
        this.addProperty(PROPERTY_REQUEST_ID, requestId);
        return this;
    }

    public CxfEventBuilder setResponseCode(Integer responseCode) {
        if (responseCode == null) {
            return this;
        }
        this.addProperty(PROPERTY_RESPONSE_CODE, responseCode);
        return this;
    }

    @Override
    protected String getType() {
        return TYPE;
    }
}
