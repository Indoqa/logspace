/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.indoqa.commons.lang.util.TimeUtils;

public class FacetBuilder {

    private List<Facet> facets = new ArrayList<>();

    public static void main(String[] args) throws ParseException {
        FacetBuilder facetBuilder = new FacetBuilder();

        Facet valueFacet = StatisticFacet.with("value", "max(long_property_duration)");

        Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2013-01-01 00:00:00");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-01-01 00:00:00");
        int gap = (int) TimeUnit.DAYS.toSeconds(1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate)) {
            String name = String.valueOf(facetBuilder.getFacetCount() + 1);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("timestamp:[");
            stringBuilder.append(TimeUtils.formatSolrDate(calendar.getTime()));
            stringBuilder.append(" TO ");
            calendar.add(Calendar.SECOND, gap);
            stringBuilder.append(TimeUtils.formatSolrDate(calendar.getTime()));
            stringBuilder.append("]");
            String query = stringBuilder.toString();

            facetBuilder.addFacet(QueryFacet.with(name, query, valueFacet));
        }

        System.out.println(facetBuilder.toJson());
    }

    public void addFacet(Facet facet) {
        this.facets.add(facet);
    }

    public int getFacetCount() {
        return this.facets.size();
    }

    public String toJson() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append('{');

        for (Facet eachFacet : this.facets) {
            eachFacet.append(stringBuilder);
            stringBuilder.append(',');
        }

        if (!this.facets.isEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        stringBuilder.append('}');

        return stringBuilder.toString();
    }

    public abstract static class Facet {

        private String name;
        private List<Facet> children;

        public List<Facet> getChildren() {
            return this.children;
        }

        public final String getName() {
            return this.name;
        }

        public void addChild(Facet facet) {
            if (this.children == null) {
                this.children = new ArrayList<>();
            }

            this.children.add(facet);
        }

        public void setChildren(List<Facet> children) {
            this.children = children;
        }

        public final void setName(String name) {
            this.name = name;
        }

        protected void append(StringBuilder stringBuilder) {
            stringBuilder.append("\"");
            stringBuilder.append(this.name);
            stringBuilder.append("\":");

            this.appendBody(stringBuilder);
        }

        protected void appendChildren(StringBuilder stringBuilder) {
            if (this.children == null || this.children.isEmpty()) {
                return;
            }

            stringBuilder.append(',');
            stringBuilder.append("facet:{");
            for (Facet eachChild : this.children) {
                eachChild.append(stringBuilder);
            }
            stringBuilder.append("}");
        }

        protected abstract void appendBody(StringBuilder stringBuilder);
    }

    public static class QueryFacet extends Facet {

        private String query;

        public static QueryFacet with(String name, String query, Facet... children) {
            QueryFacet result = new QueryFacet();

            result.setName(name);
            result.setQuery(query);

            for (Facet eachChild : children) {
                result.addChild(eachChild);
            }

            return result;
        }

        public String getQuery() {
            return this.query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        @Override
        protected void appendBody(StringBuilder stringBuilder) {
            stringBuilder.append('{');
            stringBuilder.append("query:{q:");
            stringBuilder.append('"');
            stringBuilder.append(this.query);
            stringBuilder.append('"');
            this.appendChildren(stringBuilder);
            stringBuilder.append("}}");
        }
    }

    public static class StatisticFacet extends Facet {

        private String statistic;

        public static StatisticFacet with(String name, String statistic) {
            StatisticFacet result = new StatisticFacet();

            result.setName(name);
            result.setStatistic(statistic);

            return result;
        }

        public void setStatistic(String statistic) {
            this.statistic = statistic;
        }

        @Override
        protected void appendBody(StringBuilder stringBuilder) {
            stringBuilder.append('"');
            stringBuilder.append(this.statistic);
            stringBuilder.append('"');
        }
    }
}