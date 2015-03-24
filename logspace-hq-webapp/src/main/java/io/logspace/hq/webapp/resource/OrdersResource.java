/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.webapp.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import com.indoqa.boot.AbstractJsonResourcesBase;

@Named
public class OrdersResource extends AbstractJsonResourcesBase {

    private static final String RFC1123 = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final String RFC850 = "EEEE, dd-MMM-yy HH:mm:ss z";
    private static final String ANSI_C = "EEE MMM d HH:mm:ss YYYY";

    private static final String PARAMETER_CONTROLLER_ID = "controller-id";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static DateFormat createDateFormat(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat;
    }

    private static String formatHttpDate(Date date) {
        return createDateFormat(RFC1123).format(date);
    }

    private static Date parseHttpDate(String ifModifiedSince) {
        if (ifModifiedSince == null) {
            return null;
        }

        try {
            return createDateFormat(RFC1123).parse(ifModifiedSince);
        } catch (ParseException e) {
            // do nothing
        }

        try {
            return createDateFormat(RFC850).parse(ifModifiedSince);
        } catch (ParseException e) {
            // do nothing
        }

        try {
            return createDateFormat(ANSI_C).parse(ifModifiedSince);
        } catch (ParseException e) {
            // do nothing
        }

        return null;
    }

    @PostConstruct
    public void mount() {
        Spark.get("/orders/:" + PARAMETER_CONTROLLER_ID, "application/json", (Route) (req, res) -> this.getOrder(req, res));
    }

    private File getConfigFile(String controllerId) {
        return new File(MessageFormat.format("./configs/{0}.json", controllerId));
    }

    private String getOrder(Request req, Response res) throws IOException {
        String controllerId = req.params(PARAMETER_CONTROLLER_ID);
        this.logger.debug("Retrieving order for AgentController with ID '{}'.", controllerId);

        File file = this.getConfigFile(controllerId);
        if (!file.exists()) {
            res.status(404);
            return "";
        }

        Date ifModifiedSince = parseHttpDate(req.headers("If-Modified-Since"));
        if (ifModifiedSince != null && ifModifiedSince.getTime() / 1000 == file.lastModified() / 1000) {
            res.status(304);
            return "";
        }

        try (InputStream inputStream = new FileInputStream(file)) {
            res.header("Last-Modified", formatHttpDate(new Date(file.lastModified())));
            return IOUtils.toString(inputStream, "UTF-8");
        }
    }
}
