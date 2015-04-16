/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api;

public class DataDefinition {

    private DateRange dateRange;

    private String space;
    private String agentId;
    private String propertyId;

    private Aggregate aggregate;

    public String getAgentId() {
        return this.agentId;
    }

    public Aggregate getAggregate() {
        return this.aggregate;
    }

    public DateRange getDateRange() {
        return this.dateRange;
    }

    public String getPropertyId() {
        return this.propertyId;
    }

    public String getSpace() {
        return this.space;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public void setAggregate(Aggregate aggregate) {
        this.aggregate = aggregate;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public static enum Aggregate {
        max, min, average, count, sum;
    }
}
