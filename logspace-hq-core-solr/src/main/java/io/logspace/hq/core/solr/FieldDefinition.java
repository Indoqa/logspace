/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr;

public final class FieldDefinition {

    private final String solrFieldName;
    private final String apiFieldName;

    public FieldDefinition(String solrFieldName, String apiFieldName) {
        super();
        this.solrFieldName = solrFieldName;
        this.apiFieldName = apiFieldName;
    }

    public String getApiFieldName() {
        return this.apiFieldName;
    }

    public String getSolrFieldName() {
        return this.solrFieldName;
    }
}
