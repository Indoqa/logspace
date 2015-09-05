/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp.resource;

import io.logspace.hq.core.api.DateRange;

public class DataResponse {

    private DateRange dateRange;

    private Object[][] data;

    public Object[][] getData() {
        return this.data;
    }

    public DateRange getDateRange() {
        return this.dateRange;
    }

    public void setData(Object[][] data) {
        this.data = data.clone();
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }
}
