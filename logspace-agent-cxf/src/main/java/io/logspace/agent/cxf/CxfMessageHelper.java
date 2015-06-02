/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.cxf;

import java.util.List;
import java.util.Map;

import org.apache.cxf.message.Message;

public class CxfMessageHelper {

    private CxfMessageHelper() {
        // hide utility class constructor
    }

    public static String getProtocolHeaderValue(Message message, String header) {
        @SuppressWarnings("unchecked")
        Map<String, List<?>> headers = (Map<String, List<?>>) message.get(Message.PROTOCOL_HEADERS);
        if (headers == null) {
            return null;
        }

        List<?> headerValues = headers.get(header);
        if (headerValues == null || headerValues.isEmpty()) {
            return null;
        }

        Object headerValue = headerValues.get(0);
        if (headerValue == null) {
            return null;
        }

        return String.valueOf(headerValue);
    }
}
