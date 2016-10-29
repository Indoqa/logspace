/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr.utils;

import java.util.Date;

import org.apache.solr.common.SolrDocument;

public final class SolrDocumentHelper {

    private SolrDocumentHelper() {
        // hide utility class constructor
    }

    public static boolean getBoolean(SolrDocument solrDocument, String fieldName) {
        Boolean result = (Boolean) solrDocument.getFieldValue(fieldName);
        if (result == null) {
            return false;
        }

        return result.booleanValue();
    }

    public static Date getDate(SolrDocument solrDocument, String fieldName) {
        return (Date) solrDocument.getFirstValue(fieldName);
    }

    public static String getString(SolrDocument solrDocument, String fieldName) {
        return (String) solrDocument.getFirstValue(fieldName);
    }
}
