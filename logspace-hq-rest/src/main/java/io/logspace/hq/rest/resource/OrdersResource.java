/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.resource;

import io.logspace.hq.core.api.NotModifiedException;
import io.logspace.hq.rest.model.OrderNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;

import spark.Request;
import spark.Response;
import spark.Spark;

@Named
public class OrdersResource extends AbstractSpaceResource {

    private static final String RFC1123 = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final String RFC850 = "EEEE, dd-MMM-yy HH:mm:ss z";
    private static final String ANSI_C = "EEE MMM d HH:mm:ss YYYY";

    private static final String PARAMETER_CONTROLLER_ID = "controller-id";

    @Value("${logspace.hq-webapp.orders-directory}")
    private String ordersDirectory;

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
        Spark.get("/orders/:" + PARAMETER_CONTROLLER_ID, "application/json", (req, res) -> this.getOrder(req, res));
    }

    private String getOrder(Request req, Response res) throws IOException {
        this.validateSpace(req);

        String controllerId = req.params(PARAMETER_CONTROLLER_ID);

        File file = this.getOrderFile(controllerId);
        if (!file.exists()) {
            throw new OrderNotFoundException("There is no order for controller with ID '" + controllerId + "'.");
        }

        Date ifModifiedSince = parseHttpDate(req.headers("If-Modified-Since"));
        if (ifModifiedSince != null && ifModifiedSince.getTime() / 1000 == file.lastModified() / 1000) {
            throw new NotModifiedException();
        }

        this.logger.info("Serving order for AgentController with ID '{}'.", controllerId);
        try (InputStream inputStream = new FileInputStream(file)) {
            res.header("Last-Modified", formatHttpDate(new Date(file.lastModified())));
            return IOUtils.toString(inputStream, "UTF-8");
        }
    }

    private File getOrderFile(String controllerId) {
        return new File(this.ordersDirectory, controllerId + ".json");
    }
}
