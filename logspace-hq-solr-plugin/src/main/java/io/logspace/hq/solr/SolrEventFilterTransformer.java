/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.StringUtils;

import com.indoqa.lang.util.TimeUtils;

import io.logspace.hq.rest.api.event.*;

/*default*/ final class SolrEventFilterTransformer {

    private static Map<Class<? extends EventFilterElement>, BiConsumer<EventFilterElement, StringBuilder>> TRANSFORMERS = new HashMap<>();

    private static BiConsumer<EventFilterElement, StringBuilder> TRANSFORM_EQUALS_FILTER = (element, queryBuilder) -> {
        EqualsEventFilterElement equalsEventFilterElement = (EqualsEventFilterElement) element;
        appendProperty(equalsEventFilterElement, queryBuilder);
        appendSolrValue(queryBuilder, equalsEventFilterElement.getValue());
    };

    private static BiConsumer<EventFilterElement, StringBuilder> TRANSFORM_RANGE_FILTER = (element, queryBuilder) -> {
        RangeEventFilterElement rangeEventFilterElement = (RangeEventFilterElement) element;
        appendProperty(rangeEventFilterElement, queryBuilder);
        queryBuilder.append('[');
        appendSolrValue(queryBuilder, rangeEventFilterElement.getFrom());
        queryBuilder.append(" TO ");
        appendSolrValue(queryBuilder, rangeEventFilterElement.getTo());
        queryBuilder.append(']');
    };

    private static BiConsumer<EventFilterElement, StringBuilder> TRANSFORM_MULTI_FILTER = (element, queryBuilder) -> {
        MultiValueEventFilterElement multiValueEventFilterElement = (MultiValueEventFilterElement) element;
        appendProperty(multiValueEventFilterElement, queryBuilder);
        queryBuilder.append('(');
        for (Iterator<String> iterator = multiValueEventFilterElement.getValues().iterator(); iterator.hasNext();) {
            appendSolrValue(queryBuilder, iterator.next());

            if (iterator.hasNext()) {
                queryBuilder.append(' ');
                queryBuilder.append(multiValueEventFilterElement.getOperator());
                queryBuilder.append(' ');
            }
        }
        queryBuilder.append(')');
    };

    static {
        TRANSFORMERS.put(EqualsEventFilterElement.class, TRANSFORM_EQUALS_FILTER);
        TRANSFORMERS.put(RangeEventFilterElement.class, TRANSFORM_RANGE_FILTER);
        TRANSFORMERS.put(MultiValueEventFilterElement.class, TRANSFORM_MULTI_FILTER);
    }

    private SolrEventFilterTransformer() {
        // hide utility class constructor
    }

    public static String transform(EventFilter eventFilter) {
        StringBuilder queryBuilder = new StringBuilder();

        for (EventFilterElement eventFilterElement : eventFilter) {
            transform(queryBuilder, eventFilterElement);
        }

        return queryBuilder.toString();
    }

    public static String transform(EventFilterElement eventFilterElement) {
        StringBuilder queryBuilder = new StringBuilder();
        transform(queryBuilder, eventFilterElement);
        return queryBuilder.toString();
    }

    public static void transform(StringBuilder queryBuilder, EventFilterElement eventFilterElement) {
        BiConsumer<EventFilterElement, StringBuilder> biConsumer = TRANSFORMERS.get(eventFilterElement.getClass());
        if (biConsumer != null) {
            biConsumer.accept(eventFilterElement, queryBuilder);
        }
    }

    private static void appendProperty(PropertyEventFilterElement element, StringBuilder queryBuilder) {
        queryBuilder.append(element.getProperty());
        queryBuilder.append(':');
    }

    private static void appendSolrValue(StringBuilder stringBuilder, Object value) {
        if (value == null) {
            stringBuilder.append('*');
            return;
        }

        if (value instanceof Date) {
            stringBuilder.append(TimeUtils.formatSolrDate((Date) value));
        }

        String result = String.valueOf(value);
        if (StringUtils.isBlank(result)) {
            stringBuilder.append('*');
            return;
        }

        stringBuilder.append('"');
        stringBuilder.append(result);
        stringBuilder.append('"');
    }
}
