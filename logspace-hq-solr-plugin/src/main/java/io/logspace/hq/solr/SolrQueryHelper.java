/**
 * Logspace
 * Copyright (c) 2016 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

/*default*/ final class SolrQueryHelper {

    public static final String ALL_DOCS_QUERY = "*:*";
    public static final String SORT_CRON_ASC = "timestamp ASC, id ASC";
    public static final String SORT_CRON_DESC = "timestamp DESC, id ASC";

    public static final String VALUE_FACET_NAME = "val";
    public static final String AGGREGATION_FACET_NAME = "agg";
    public static final String COUNT_FACET_NAME = "count";

    private SolrQueryHelper() {
        // hide utility class constructor
    }
}
