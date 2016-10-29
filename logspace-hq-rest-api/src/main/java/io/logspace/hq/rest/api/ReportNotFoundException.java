/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api;

import static io.logspace.hq.rest.api.HttpStatusCode.NotFound;

public final class ReportNotFoundException extends AbstractLogspaceResourceException {

    private static final long serialVersionUID = 1L;

    private ReportNotFoundException(String message) {
        super(message, NotFound, "REPORT_NOT_FOUND");
    }

    public static ReportNotFoundException forReportId(String reportId) {
        ReportNotFoundException result = new ReportNotFoundException("There is no report with ID '" + reportId + "'.");
        result.setParameter("report-id", reportId);
        return result;
    }
}
