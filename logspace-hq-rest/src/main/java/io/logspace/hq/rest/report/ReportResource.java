/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.report;

import static io.logspace.hq.rest.api.HttpStatusCode.NoContent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import io.logspace.hq.core.api.report.ReportService;
import io.logspace.hq.rest.AbstractLogspaceResourcesBase;
import io.logspace.hq.rest.api.ReportNotFoundException;
import io.logspace.hq.rest.api.report.Report;
import io.logspace.hq.rest.api.report.ReportHistory;
import io.logspace.hq.rest.api.report.Reports;
import spark.Request;
import spark.Response;

@Named
public class ReportResource extends AbstractLogspaceResourcesBase {

    private static final String PARAMETER_ID = "id";

    private static final String PARAMETER_START = "start";
    private static final int DEFAULT_START = 0;
    private static final int MIN_START = 0;
    private static final int MAX_START = Integer.MAX_VALUE;

    private static final String PARAMETER_COUNT = "count";
    private static final int DEFAULT_COUNT = 10;
    private static final int MIN_COUNT = 0;
    private static final int MAX_COUNT = 1000;

    private static final String PARAMETER_SORT = "sort";
    private static final String DEFAULT_SORT = "name asc";

    @Inject
    private ReportService reportService;

    @PostConstruct
    public void mount() {
        this.get("/v1/reports", (req, res) -> this.getReports(req));
        this.get("/v1/reports/:" + PARAMETER_ID, (req, res) -> this.getReport(req));
        this.get("/v1/reports/:" + PARAMETER_ID + "/history", (req, res) -> this.getReportHistory(req));

        this.post("/v1/reports", (req, res) -> this.postReport(req));

        this.delete("/v1/reports/:" + PARAMETER_ID, (req, res) -> this.deleteReport(req, res));
    }

    private Void deleteReport(Request req, Response res) {
        String reportId = req.params(PARAMETER_ID);

        this.reportService.deleteReport(reportId);

        res.status(NoContent.getCode());
        return null;
    }

    private Report getReport(Request req) {
        String reportId = req.params(PARAMETER_ID);

        Report result = this.reportService.getReport(reportId);
        if (result == null) {
            throw ReportNotFoundException.forReportId(reportId);
        }

        return result;
    }

    private ReportHistory getReportHistory(Request req) {
        String reportId = req.params(PARAMETER_ID);

        ReportHistory result = this.reportService.getReportHistory(reportId);
        if (result == null) {
            throw ReportNotFoundException.forReportId(reportId);
        }

        return result;
    }

    private Reports getReports(Request req) {
        int start = getQueryParam(req, PARAMETER_START, DEFAULT_START, MIN_START, MAX_START);
        int count = getQueryParam(req, PARAMETER_COUNT, DEFAULT_COUNT, MIN_COUNT, MAX_COUNT);
        String sort = getQueryParam(req, PARAMETER_SORT, DEFAULT_SORT);

        return this.reportService.getReports(start, count, sort);
    }

    private Report postReport(Request req) {
        Report report = this.getTransformer().toObject(req.body(), Report.class);

        this.reportService.saveReport(report);

        return report;
    }
}
