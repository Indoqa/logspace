/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.solr;

public class StatisticFacet extends AbstractFacet {

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
