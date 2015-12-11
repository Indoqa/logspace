/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import com.indoqa.boot.AbstractJsonResourcesBase;

import io.logspace.hq.rest.api.ParameterValueException;
import spark.Request;

public abstract class AbstractLogspaceResourcesBase extends AbstractJsonResourcesBase {

    @Value("${logspace.hq-rest.base-path}")
    private String basePath;

    protected static int getQueryParam(Request request, String name, int defaultValue, int minValue, int maxValue) {
        String value = StringUtils.trim(request.queryParams(name));
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }

        int result;
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw ParameterValueException.unparsableValue(name, value, e);
        }

        if (result < minValue) {
            throw ParameterValueException.valueTooSmall(name, result, minValue);
        }

        if (result > maxValue) {
            throw ParameterValueException.valueTooLarge(name, result, maxValue);
        }

        return result;
    }

    protected static String getQueryParam(Request req, String name, String defaultValue) {
        String value = StringUtils.trim(req.queryParams(name));
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }

        return value;
    }

    protected static String getRequiredQueryParam(Request req, String name) {
        String value = req.queryParams(name);
        if (StringUtils.isBlank(value)) {
            throw ParameterValueException.missingQueryParameter(name);
        }

        return value;
    }

    @Override
    protected CharSequence getResourceBase() {
        return this.basePath;
    }
}
