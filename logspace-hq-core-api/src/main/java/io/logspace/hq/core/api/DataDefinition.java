/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api;

import io.logspace.agent.api.order.Aggregate;

public class DataDefinition {

    private DateRange dateRange;

    private String globalAgentId;
    private String propertyId;

    private Aggregate aggregate;

    public Aggregate getAggregate() {
        return this.aggregate;
    }

    public DateRange getDateRange() {
        return this.dateRange;
    }

    public String getFacetFunction() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.getAggregate());
        stringBuilder.append("(");
        stringBuilder.append(this.getPropertyId());
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    public String getGlobalAgentId() {
        return this.globalAgentId;
    }

    public String getPropertyId() {
        return this.propertyId;
    }

    public void setAggregate(Aggregate aggregate) {
        this.aggregate = aggregate;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public void setGlobalAgentId(String globalAgentId) {
        this.globalAgentId = globalAgentId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }
}