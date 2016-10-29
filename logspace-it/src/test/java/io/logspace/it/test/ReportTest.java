/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.it.test;

import static org.junit.Assert.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;

import io.logspace.agent.api.order.Aggregate;
import io.logspace.hq.rest.api.ParameterValueException;
import io.logspace.hq.rest.api.report.Report;
import io.logspace.hq.rest.api.report.ReportHistory;
import io.logspace.hq.rest.api.timeseries.TimeSeriesDefinition;
import io.logspace.hq.rest.api.timeseries.TimeSeriesDefinitions;
import io.logspace.it.AbstractLogspaceTest;

public class ReportTest extends AbstractLogspaceTest {

    @Test
    public void continueAtNonTipInNewBranch() {
        Report report = new Report();
        report.setName("Test-Report-4");
        this.getReportService().saveReport(report);

        Report nextReport = new Report();
        nextReport.setName("Test-Report-5");
        nextReport.setParentId(report.getId());
        nextReport.setBranch(report.getBranch());
        this.getReportService().saveReport(nextReport);

        this.commitConfigSolr();

        Report testReport = new Report();
        testReport.setName("Test-Report6");
        testReport.setParentId(report.getId());
        this.getReportService().saveReport(testReport);

        assertNotNull(testReport.getId());
    }

    @Test
    public void continueAtNonTipInSameBranch() {
        Report report = new Report();
        report.setName("Test-Report-7");
        this.getReportService().saveReport(report);

        Report nextReport = new Report();
        nextReport.setName("Test-Report-8");
        nextReport.setParentId(report.getId());
        nextReport.setBranch(report.getBranch());
        this.getReportService().saveReport(nextReport);

        this.commitConfigSolr();

        Report invalidReport = new Report();
        invalidReport.setName("Invalid Report");
        invalidReport.setParentId(report.getId());
        invalidReport.setBranch(report.getBranch());
        try {
            this.getReportService().saveReport(invalidReport);
            fail("Expected exception");
        } catch (ParameterValueException e) {
            assertEquals("INVALID_PARAMETER_VALUE", e.getErrorData().getType());
            assertEquals("NOT_TIP_OF_BRANCH", e.getErrorData().getParameter("reason"));
        }

        assertNull(invalidReport.getId());
    }

    @Test
    public void continueBranch() {
        Report report = new Report();
        report.setName("Test-Report-4");
        this.getReportService().saveReport(report);

        Report nextReport = new Report();
        nextReport.setName("Test-Report-5");
        nextReport.setParentId(report.getId());
        nextReport.setBranch(report.getBranch());
        this.getReportService().saveReport(nextReport);

        assertNotNull(nextReport.getBranch());
        assertEquals(report.getBranch(), nextReport.getBranch());

        ReportHistory reportHistory = this.getReportService().getReportHistory(report.getId());
        assertNotNull(reportHistory);
        assertEquals(1, reportHistory.getHistory().size());
        this.compareReports(report, reportHistory.getHistory().get(0));

        ReportHistory nextReportHistory = this.getReportService().getReportHistory(nextReport.getId());
        assertNotNull(nextReportHistory);
        assertEquals(2, nextReportHistory.getHistory().size());
        this.compareReports(nextReport, nextReportHistory.getHistory().get(0));
        this.compareReports(report, nextReportHistory.getHistory().get(1));
    }

    @Test
    public void createNewBranch() {
        Report report = new Report();
        report.setName("Test-Report-2");
        this.getReportService().saveReport(report);

        Report nextReport = new Report();
        nextReport.setName("Test-Report-3");
        nextReport.setParentId(report.getId());
        this.getReportService().saveReport(nextReport);

        assertNotNull(nextReport.getBranch());
        assertNotEquals(report.getBranch(), nextReport.getBranch());

        ReportHistory reportHistory = this.getReportService().getReportHistory(report.getId());
        assertNotNull(reportHistory);
        assertEquals(1, reportHistory.getHistory().size());
        this.compareReports(report, reportHistory.getHistory().get(0));

        ReportHistory nextReportHistory = this.getReportService().getReportHistory(nextReport.getId());
        assertNotNull(nextReportHistory);
        assertEquals(2, nextReportHistory.getHistory().size());
        this.compareReports(nextReport, nextReportHistory.getHistory().get(0));
        this.compareReports(report, nextReportHistory.getHistory().get(1));
    }

    @Test
    public void createReport() {
        Report report = new Report();
        report.setName("Test-Report-1");

        TimeSeriesDefinition timeSeriesDefinition = new TimeSeriesDefinition();
        timeSeriesDefinition.setAggregate(Aggregate.avg);
        timeSeriesDefinition.setGlobalAgentId("development|localhost|test-agent");
        timeSeriesDefinition.setPropertyId("property_id");
        report.setTimeSeriesDefinitions(TimeSeriesDefinitions.create(timeSeriesDefinition));

        this.getReportService().saveReport(report);

        assertNotNull(report.getId());
        assertNotNull(report.getBranch());
        assertNotNull(report.getTimestamp());
        assertNull(report.getParentId());

        Report loadedReport = this.getReportService().getReport(report.getId());
        this.compareReports(report, loadedReport);
    }

    @Test
    public void referenceMissingParent() {
        Report report = new Report();
        report.setName("Test-Report");
        report.setParentId("unknown-report-id");

        try {
            this.getReportService().saveReport(report);
            fail("Expected exception.");
        } catch (ParameterValueException e) {
            assertEquals("INVALID_PARAMETER_VALUE", e.getErrorData().getType());
            assertEquals("UNKNOWN_PARENT", e.getErrorData().getParameter("reason"));
        }

        assertNull(report.getId());
    }

    @Test
    public void saveWithId() {
        Report report = new Report();
        report.setName("Test-Report-9");
        this.getReportService().saveReport(report);

        assertNotNull(report.getId());

        report.setName("Changed Name");
        try {
            this.getReportService().saveReport(report);
            fail("Expected exception");
        } catch (ParameterValueException e) {
            assertEquals("INVALID_PARAMETER_VALUE", e.getErrorData().getType());
            assertEquals("ID_MUST_BE_OMITTED", e.getErrorData().getParameter("reason"));
        }

        Report loadedReport = this.getReportService().getReport(report.getId());
        assertNotEquals(report.getName(), loadedReport.getName());
    }

    private void compareReports(Report expected, Report actual) {
        assertTrue("The loaded report does not equal the stored one.",
            EqualsBuilder.reflectionEquals(actual, expected, "timeSeriesDefinitions"));

        if (expected.getTimeSeriesDefinitions() == actual.getTimeSeriesDefinitions()) {
            return;
        }

        assertEquals("The loaded report does not equal the stored one.", actual.getTimeSeriesDefinitions().getDefinitionCount(),
            expected.getTimeSeriesDefinitions().getDefinitionCount());

        for (int i = 0; i < actual.getTimeSeriesDefinitions().getDefinitionCount(); i++) {
            assertTrue("The loaded report does not equal the stored one.", EqualsBuilder.reflectionEquals(
                actual.getTimeSeriesDefinitions().getDefinition(i), expected.getTimeSeriesDefinitions().getDefinition(i)));
        }
    }
}
