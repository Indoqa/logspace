/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import spark.Request;

import com.indoqa.boot.AbstractJsonResourcesBase;

public abstract class AbstractLogspaceResourcesBase extends AbstractJsonResourcesBase {

    @Value("${logspace.hq-rest.base-path}")
    private String basePath;

    protected static String getParam(Request req, String name, String defaultValue) {
        String value = StringUtils.trim(req.params(name));
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }

        return value;
    }

    protected static String getRequiredParam(Request req, String name) {
        String value = req.params(name);
        if (StringUtils.isBlank(value)) {
            throw ParameterValueException.missingQueryParameter(name);
        }

        return value;
    }

    protected static int getParam(Request request, String name, int defaultValue, int minValue, int maxValue) {
        String value = StringUtils.trim(request.params(name));
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
    
        int result;
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw ParameterValueException.unparsableValue(name, value);
        }
    
        if (result < minValue) {
            throw ParameterValueException.valueTooSmall(name, result, minValue);
        }
    
        if (result > maxValue) {
            throw ParameterValueException.valueTooLarge(name, result, maxValue);
        }
    
        return result;
    }

    @Override
    protected CharSequence getResourceBase() {
        return this.basePath;
    }
}
