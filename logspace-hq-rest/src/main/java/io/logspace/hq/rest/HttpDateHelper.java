/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class HttpDateHelper {

    public static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String HEADER_LAST_MODIFIED = "Last-Modified";

    private static final String DATE_FORMAT_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final String DATE_FORMAT_RFC850 = "EEEE, dd-MMM-yy HH:mm:ss z";
    private static final String DATE_FORMAT_ANSI_C = "EEE MMM d HH:mm:ss YYYY";

    private HttpDateHelper() {
        // hide utility class constructor
    }

    public static String formatHttpDate(Date date) {
        return createDateFormat(DATE_FORMAT_RFC1123).format(date);
    }

    public static Date parseHttpDate(String ifModifiedSince) {
        if (ifModifiedSince == null) {
            return null;
        }

        try {
            return createDateFormat(DATE_FORMAT_RFC1123).parse(ifModifiedSince);
        } catch (ParseException e) {
            // do nothing
        }

        try {
            return createDateFormat(DATE_FORMAT_RFC850).parse(ifModifiedSince);
        } catch (ParseException e) {
            // do nothing
        }

        try {
            return createDateFormat(DATE_FORMAT_ANSI_C).parse(ifModifiedSince);
        } catch (ParseException e) {
            // do nothing
        }

        return null;
    }

    private static DateFormat createDateFormat(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat;
    }

}
