/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Date;

public class DateRange {

    private Date start;
    private Date end;

    private int gap;

    public Date getEnd() {
        return this.end;
    }

    public int getGap() {
        return this.gap;
    }

    public Date getStart() {
        return this.start;
    }

    public int getSteps() {
        long gapInMilliseconds = SECONDS.toMillis(this.gap);
        return (int) ((this.end.getTime() - this.start.getTime() + gapInMilliseconds - 1) / gapInMilliseconds);
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public void setStart(Date start) {
        this.start = start;
    }
}
