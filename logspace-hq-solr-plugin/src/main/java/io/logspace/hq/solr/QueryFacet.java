/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

public class QueryFacet extends AbstractFacet {

    private String query;

    public static QueryFacet with(String name, String query, Facet child) {
        QueryFacet result = new QueryFacet();

        result.setName(name);
        result.setQuery(query);

        if (child != null) {
            result.addChild(child);
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