/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.solr.report;

import static io.logspace.hq.core.solr.ConfigFieldConstants.*;
import static io.logspace.hq.core.solr.report.TimeSeriesDefinitionJsonHelper.*;
import static io.logspace.hq.core.solr.utils.SolrDocumentHelper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Named;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;

import io.logspace.hq.core.api.report.ReportService;
import io.logspace.hq.core.solr.AbstractSolrConfigService;
import io.logspace.hq.core.solr.FieldDefinition;
import io.logspace.hq.core.solr.FieldDefinitions;
import io.logspace.hq.core.solr.utils.SolrQueryHelper;
import io.logspace.hq.rest.api.*;
import io.logspace.hq.rest.api.report.Report;
import io.logspace.hq.rest.api.report.ReportHistory;
import io.logspace.hq.rest.api.report.Reports;

@Named
public class SolrReportService extends AbstractSolrConfigService implements ReportService {

    private static final String CONFIG_TYPE = "report";

    private static final String FIELD_DELETED = "boolean_property_deleted";
    private static final String FIELD_BRANCH = "string_property_branch";
    private static final String FIELD_PARENT_ID = "string_property_parent_id";

    private static final String FILTER_REPORT = FIELD_TYPE + ":" + CONFIG_TYPE;
    private static final String FILTER_UNDELETED = "-" + FIELD_DELETED + ":*";
    private static final String FILTER_TIP_OF_BRANCH = "{!collapse field=string_property_branch max=_version_}";

    private static Report createReport(SolrDocument solrDocument) throws IOException {
        Report result = new Report();

        result.setId(getString(solrDocument, FIELD_ID));
        result.setBranch(getString(solrDocument, FIELD_BRANCH));
        result.setParentId(getString(solrDocument, FIELD_PARENT_ID));
        result.setDeleted(getBoolean(solrDocument, FIELD_DELETED));
        result.setName(getString(solrDocument, FIELD_NAME));
        result.setTimestamp(getDate(solrDocument, FIELD_TIMESTAMP));
        result.setTimeSeriesDefinitions(deserializeDefinitions(getString(solrDocument, FIELD_CONTENT)));

        return result;
    }

    @Override
    public void deleteReport(String reportId) {
        this.logger.debug("Deleting report with ID '{}'.", reportId);

        try {
            SolrInputDocument solrInputDocument = new SolrInputDocument();
            solrInputDocument.setField(FIELD_ID, reportId);

            // make this a field update to an existing document
            solrInputDocument.setField(FIELD_DELETED, asFieldUpdate(Boolean.TRUE));
            documentMustExist(solrInputDocument);

            this.solrClient.add(solrInputDocument);

            this.makeChangesVisible();
        } catch (SolrException e) {
            if (HttpStatusCode.CONFLICT.matches(e.code())) {
                // this means that the ID does not exist -> throw appropriate exception
                throw ReportNotFoundException.forReportId(reportId);
            }
        } catch (SolrServerException | IOException e) {
            throw new DataDeletionException("Could not delete report.", e);
        }
    }

    @Override
    public Report getReport(String reportId) {
        this.logger.debug("Retrieving report with ID '{}'.", reportId);

        try {
            SolrDocument solrDocument = this.realTimeGet(reportId);
            if (solrDocument == null) {
                return null;
            }

            return createReport(solrDocument);
        } catch (SolrServerException | IOException e) {
            throw new DataRetrievalException("Could not retrieve report.", e);
        }
    }

    @Override
    public ReportHistory getReportHistory(String reportId) {
        this.logger.debug("Retrieving history for report with ID '{}'.", reportId);
        List<Report> history = new ArrayList<>();

        String nextReportId = reportId;
        while (nextReportId != null) {
            Report report = this.getReport(nextReportId);

            if (report == null) {
                Report missingReport = new Report();
                missingReport.setId(nextReportId);
                missingReport.setName("[Missing Report]");
                history.add(missingReport);

                break;
            }

            history.add(report);
            nextReportId = report.getParentId();
        }

        return ReportHistory.create(history);
    }

    @Override
    public Reports getReports(int start, int count, String sort) {
        this.logger.debug("Retrieving {} reports from {}, sorted by '{}'.", count, start, sort);

        SolrQuery solrQuery = new SolrQuery("*:*");

        solrQuery.addFilterQuery(FILTER_REPORT);
        solrQuery.addFilterQuery(FILTER_UNDELETED);
        solrQuery.addFilterQuery(FILTER_TIP_OF_BRANCH);

        SolrQueryHelper.addSort(solrQuery, sort, this.getFieldDefinitions());
        SolrQueryHelper.setRange(solrQuery, start, count);

        try {
            List<Report> reports = new ArrayList<>();

            QueryResponse queryResponse = this.solrClient.query(solrQuery);
            SolrDocumentList results = queryResponse.getResults();
            for (SolrDocument eachSolrDocument : results) {
                reports.add(createReport(eachSolrDocument));
            }

            return Reports.create(reports, start, Math.toIntExact(results.getNumFound()));
        } catch (SolrServerException | IOException e) {
            throw new DataRetrievalException("Could not retrieve reports.", e);
        }
    }

    @Override
    public void saveReport(Report report) {
        this.validateReportCanBeSaved(report);

        this.logger.debug("Storing report with ID '{}'.", report.getId());

        for (String id : new EscalatingIdGenerator("report_", 3)) {
            String branch = report.getBranch() != null ? report.getBranch() : id;
            Date timestamp = new Date();

            try {
                SolrInputDocument solrInputDocument = new SolrInputDocument();
                solrInputDocument.setField(FIELD_ID, id);
                solrInputDocument.setField(FIELD_BRANCH, branch);
                solrInputDocument.setField(FIELD_PARENT_ID, report.getParentId());
                solrInputDocument.setField(FIELD_TYPE, CONFIG_TYPE);
                solrInputDocument.setField(FIELD_NAME, report.getName());
                solrInputDocument.setField(FIELD_TIMESTAMP, timestamp);
                solrInputDocument.setField(FIELD_CONTENT, serializeDefinitions(report.getTimeSeriesDefinitions()));

                // fail with HTTP CONFLICT if the ID is already in use
                documentMustNotExist(solrInputDocument);
                this.solrClient.add(solrInputDocument);

                this.makeChangesVisible();

                // the add was successful -> update the report and stop here
                report.setId(id);
                report.setBranch(branch);
                report.setTimestamp(timestamp);
                break;
            } catch (SolrException e) {
                if (HttpStatusCode.CONFLICT.matches(e.code())) {
                    // this means that our generated ID is already in use -> try the next ID
                    continue;
                }

                throw new DataStorageException("Could not store report.", e);
            } catch (SolrServerException | IOException e) {
                throw new DataStorageException("Could not store report.", e);
            }
        }
    }

    @Override
    public void undeleteReport(String reportId) {
        this.logger.debug("Un-deleting report with ID '{}'.", reportId);

        try {
            SolrInputDocument solrInputDocument = new SolrInputDocument();
            solrInputDocument.setField(FIELD_ID, reportId);

            // make this a field update to an existing document
            solrInputDocument.setField(FIELD_DELETED, asFieldUpdate(null));
            documentMustExist(solrInputDocument);

            this.solrClient.add(solrInputDocument);

            this.makeChangesVisible();
        } catch (SolrException e) {
            if (HttpStatusCode.CONFLICT.matches(e.code())) {
                // this means that the id does not exist -> throw appropriate exception
                throw ReportNotFoundException.forReportId(reportId);
            }
        } catch (SolrServerException | IOException e) {
            throw new DataDeletionException("Could not un-delete report.", e);
        }
    }

    @Override
    protected FieldDefinitions createFieldDefinitions() {
        return new FieldDefinitions(new FieldDefinition(FIELD_BRANCH, "branch"), new FieldDefinition(FIELD_DELETED, "deleted"),
            new FieldDefinition(FIELD_PARENT_ID, "parentId"));
    }

    private boolean isTipOfBranch(Report report) {
        SolrQuery solrQuery = new SolrQuery("*:*");

        SolrQueryHelper.addFilterQuery(solrQuery, FIELD_BRANCH, report.getBranch());
        SolrQueryHelper.addFilterQuery(solrQuery, FIELD_PARENT_ID, report.getId());
        SolrQueryHelper.setRange(solrQuery, 0, 0);

        try {
            QueryResponse queryResponse = this.solrClient.query(solrQuery);
            return queryResponse.getResults().getNumFound() == 0;
        } catch (SolrServerException | IOException e) {
            throw new DataRetrievalException("Could not determine tip of branch.", e);
        }
    }

    private void validateReportCanBeSaved(Report report) {
        if (report.getId() != null) {
            throw ParameterValueException.invalidValue("ID_MUST_BE_OMITTED",
                "Reports are immutable and IDs will be assigned by the server.");
        }

        if (report.getParentId() == null && report.getBranch() != null) {
            throw ParameterValueException.invalidValue("BRANCH_MUST_BE_OMITTED",
                "When creating a report without parent the branch must be omitted.");
        }

        if (report.getParentId() != null) {
            Report parentReport = this.getReport(report.getParentId());
            if (parentReport == null) {
                throw ParameterValueException.invalidValue("UNKNOWN_PARENT", "The referenced parent report does not exist.");
            }

            if (report.getBranch() != null && !report.getBranch().equals(parentReport.getBranch())) {
                throw ParameterValueException.invalidValue("INVALID_BRANCH",
                    "The referenced branch does not match the branch of the parent report. "
                        + "Either use the branch of the parent to continue it or omit the branch to create a new one.");
            }

            if (report.getBranch() != null && !this.isTipOfBranch(parentReport)) {
                throw ParameterValueException.invalidValue("NOT_TIP_OF_BRANCH",
                    "The referenced parent report is not the tip of its branch. Either continue the branch from its tip or create a new branch.");
            }
        }
    }

    private static final class EscalatingIdGenerator implements Iterable<String>, Iterator<String> {

        private final String prefix;
        private int length;

        public EscalatingIdGenerator(String prefix, int length) {
            super();
            this.prefix = prefix;
            this.length = length;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Iterator<String> iterator() {
            return this;
        }

        @Override
        public String next() {
            String randomPart = RandomStringUtils.random(this.length++, "abcdefghijklmnopqrstuvwxyz0123456789");

            if (this.prefix == null) {
                return randomPart;
            }

            return this.prefix + randomPart;
        }
    }
}
