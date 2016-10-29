/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Reports implements Iterable<Report> {

    private int offset;
    private int totalCount;

    private List<Report> reports = new ArrayList<>();

    public static Reports create(List<Report> reports, int offset, int totalCount) {
        Reports result = new Reports();

        result.setReports(reports);
        result.setOffset(offset);
        result.setTotalCount(totalCount);

        return result;
    }

    public int getOffset() {
        return this.offset;
    }

    public List<Report> getReports() {
        return this.reports;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    @Override
    public Iterator<Report> iterator() {
        return this.reports.iterator();
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
