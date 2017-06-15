/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.report;

import io.logspace.hq.rest.api.report.Report;
import io.logspace.hq.rest.api.report.ReportHistory;
import io.logspace.hq.rest.api.report.Reports;

public interface ReportService {

    void deleteReport(String reportId);

    Report getReport(String reportId);

    ReportHistory getReportHistory(String reportId);

    Reports getReports(int start, int count, String sort);

    void saveReport(Report report);

    void undeleteReport(String reportId);

}
