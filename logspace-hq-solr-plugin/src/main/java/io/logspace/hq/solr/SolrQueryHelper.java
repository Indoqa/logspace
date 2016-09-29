/**
 * Logspace
 * Copyright (c) 2016 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import static com.indoqa.lang.util.StringUtils.escapeSolr;
import static com.indoqa.lang.util.TimeUtils.formatSolrDate;
import static io.logspace.hq.solr.EventFieldConstants.FIELD_TIMESTAMP;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;

import io.logspace.agent.api.order.PropertyDescription;
import io.logspace.agent.api.order.PropertyType;
import io.logspace.hq.rest.api.timeseries.TimeWindow;

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

    public static void addFilterQuery(SolrQuery solrQuery, String fieldName, String value) {
        if (StringUtils.isBlank(value)) {
            return;
        }

        solrQuery.addFilterQuery(fieldName + ":" + escapeSolr(value));
    }

    public static PropertyDescription createPropertyDescription(String propertyId) {
        if (propertyId == null) {
            return null;
        }

        Pattern propertyIdPattern = Pattern.compile("(\\w+)_property_(.*?)");
        Matcher matcher = propertyIdPattern.matcher(propertyId);
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
}
