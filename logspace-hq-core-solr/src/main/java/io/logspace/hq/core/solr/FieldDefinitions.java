/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr;

import java.util.Arrays;
import java.util.List;

public final class FieldDefinitions {

    private static final FieldDefinitions EMPTY = new FieldDefinitions();

    private final List<FieldDefinition> fieldDefinitions;

    public FieldDefinitions(FieldDefinition... fieldDefinitions) {
        this.fieldDefinitions = Arrays.asList(fieldDefinitions);
    }

    public static FieldDefinitions empty() {
        return EMPTY;
    }

    public String getApiFieldName(String solrFieldName) {
        return this.fieldDefinitions
            .stream()
            .filter(definition -> definition.getSolrFieldName().equals(solrFieldName))
            .findFirst()
            .map(FieldDefinition::getApiFieldName)
            .orElse(solrFieldName);
    }

    public String getSolrFieldName(String apiFieldName) {
        return this.fieldDefinitions
            .stream()
            .filter(definition -> definition.getApiFieldName().equals(apiFieldName))
            .findFirst()
            .map(FieldDefinition::getSolrFieldName)
            .orElse(apiFieldName);
    }
}
