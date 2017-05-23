/**
 * Logspace
 * Copyright (c) 2016 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr.utils;

import static com.indoqa.lang.util.StringUtils.escapeSolr;
import static com.indoqa.lang.util.TimeUtils.formatSolrDate;
import static io.logspace.agent.api.event.Event.FIELD_TIMESTAMP;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.SortClause;

import io.logspace.agent.api.order.PropertyDescription;
import io.logspace.agent.api.order.PropertyType;
import io.logspace.hq.core.solr.FieldDefinitions;
import io.logspace.hq.rest.api.timeseries.TimeWindow;

public final class SolrQueryHelper {

    public static final String ALL_DOCS_QUERY = "*:*";
    public static final String SORT_CRON_ASC = "timestamp ASC, id ASC";
    public static final String SORT_CRON_DESC = "timestamp DESC, id ASC";

    public static final String VALUE_FACET_NAME = "val";
    public static final String AGGREGATION_FACET_NAME = "agg";
    public static final String COUNT_FACET_NAME = "count";

    private static final Pattern PATTERN_PROPERTY_ID = Pattern.compile("(\\w+)_property_(.*?)");

    private SolrQueryHelper() {
        // hide utility class constructor
    }

    public static void addFilterQuery(SolrQuery solrQuery, String fieldName, String value) {
        if (StringUtils.isBlank(value)) {
            return;
        }

        solrQuery.addFilterQuery(fieldName + ":" + escapeSolr(value));
    }

    public static void addSort(SolrQuery solrQuery, String sort) {
        addSort(solrQuery, sort, FieldDefinitions.empty());
    }

    public static void addSort(SolrQuery solrQuery, String sort, FieldDefinitions fieldDefinitions) {
        if (sort.endsWith(" asc") || sort.endsWith(" ASC")) {
            String apiFieldName = sort.substring(0, sort.length() - 4);
            String solrFieldName = fieldDefinitions.getSolrFieldName(apiFieldName);
            solrQuery.addSort(SortClause.asc(solrFieldName));
            return;
        }

        if (sort.endsWith(" desc") || sort.endsWith(" DESC")) {
            String apiFieldName = sort.substring(0, sort.length() - 5);
            String solrFieldName = fieldDefinitions.getSolrFieldName(apiFieldName);
            solrQuery.addSort(SortClause.desc(solrFieldName));
            return;
        }

        throw new IllegalArgumentException("Invalid sort argument. Expected 'fieldname asc' or 'fieldname desc'.");
    }

    public static PropertyDescription createPropertyDescription(String propertyId) {
        if (propertyId == null) {
            return null;
        }

        Matcher matcher = PATTERN_PROPERTY_ID.matcher(propertyId);
        if (!matcher.matches()) {
            return null;
        }

        PropertyDescription result = new PropertyDescription();

        result.setId(propertyId);
        result.setPropertyType(PropertyType.get(matcher.group(1)));
        result.setName(matcher.group(2));

        return result;
    }

    public static String getTimestampRangeQuery(Date start, Date end) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(FIELD_TIMESTAMP);
        stringBuilder.append(":[");
        stringBuilder.append(formatSolrDate(start));
        stringBuilder.append(" TO ");
        stringBuilder.append(formatSolrDate(end));
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    public static String getTimestampRangeQuery(TimeWindow timeWindow) {
        return getTimestampRangeQuery(timeWindow.getStart(), timeWindow.getEnd());
    }

    public static void setRange(SolrQuery solrQuery, int start, int count) {
        solrQuery.setStart(start);
        solrQuery.setRows(count);
    }
}
