/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.report;

import java.util.Date;

import io.logspace.hq.rest.api.timeseries.TimeSeriesDefinitions;

public class Report {

    private String id;
    private String branch;
    private String parentId;

    private boolean deleted;

    private String name;

    private Date timestamp;

    private TimeSeriesDefinitions timeSeriesDefinitions;

    public String getBranch() {
        return this.branch;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getParentId() {
        return this.parentId;
    }

    public TimeSeriesDefinitions getTimeSeriesDefinitions() {
        return this.timeSeriesDefinitions;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setTimeSeriesDefinitions(TimeSeriesDefinitions timeSeriesDefinitions) {
        this.timeSeriesDefinitions = timeSeriesDefinitions;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
