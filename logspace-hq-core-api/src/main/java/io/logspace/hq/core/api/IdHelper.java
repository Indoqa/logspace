/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api;

public final class IdHelper {

    private static final char SEPARATOR = '|';

    private IdHelper() {
        // hide utility class constructor
    }

    public static String getAgentId(String globalAgentId) {
        if (globalAgentId == null) {
            return null;
        }

        int lastIndexOf = globalAgentId.lastIndexOf(SEPARATOR);
        if (lastIndexOf == -1) {
            return globalAgentId;
        }

        return globalAgentId.substring(lastIndexOf + 1);
    }

    public static String getGlobalAgentId(String space, String system, String agentId) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(space);
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(system);
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(agentId);

        return stringBuilder.toString();
    }
}
