/**
 * Logspace
 * Copyright (c) 2016 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

/*default*/ final class EventFieldConstants {

    public static final String FIELD_ID = "id";
    public static final String FIELD_SPACE = "space";
    public static final String FIELD_SYSTEM = "system";
    public static final String FIELD_AGENT_ID = "agent_id";
    public static final String FIELD_GLOBAL_AGENT_ID = "global_agent_id";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_MARKER = "marker";
    public static final String FIELD_GLOBAL_ID = "global_id";
    public static final String FIELD_PARENT_ID = "parent_id";
    public static final String FIELD_TIMESTAMP = "timestamp";
    public static final String FIELD_PROPERTY_ID = "property_id";
    public static final String FIELD_TOKENIZED_SEARCH_FIELD = "tokenized_search_field";

    private EventFieldConstants() {
        // hide constants class constructor
    }
}
