/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.report;

import java.util.List;

public class ReportHistory {

    private List<Report> history;

    public static ReportHistory create(List<Report> history) {
        ReportHistory result = new ReportHistory();
        result.setHistory(history);
        return result;
    }

    public List<Report> getHistory() {
        return this.history;
    }

    public void setHistory(List<Report> history) {
        this.history = history;
    }
}
