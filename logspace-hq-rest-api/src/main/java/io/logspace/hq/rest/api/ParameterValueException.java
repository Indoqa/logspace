/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api;

import io.logspace.agent.api.HttpStatusCode;

import java.text.MessageFormat;

public class ParameterValueException extends AbstractLogspaceResourceException {

    private static final long serialVersionUID = 1L;

    private ParameterValueException(String message, String TYPE) {
        super(message, HttpStatusCode.BadRequest, TYPE);
    }

    public static ParameterValueException missingQueryParameter(String parameterName) {
        return new ParameterValueException(MessageFormat.format("Missing query parameter ''{0}''.", parameterName),
                "MISSING_QUERY_PARAMETER_VALUE");
    }

    public static ParameterValueException unparsableValue(String parameterName, String value) {
        return new ParameterValueException(MessageFormat.format("Could not parse ''{0}'' as value for parameter ''{1}''.", value,
                parameterName), "UNPARSABLE_PARAMETER_VALUE");
    }

    public static ParameterValueException valueTooLarge(String parameterName, Object value, Object max) {
        return new ParameterValueException(MessageFormat.format(
                "Invalid value ''{0}'' for parameter ''{1}''. It is above the allowed maximum of {2}", value, parameterName, max),
                "INVALID_PARAMETER_VALUE");
    }

    public static ParameterValueException valueTooSmall(String parameterName, Object value, Object min) {
        return new ParameterValueException(MessageFormat.format(
                "Invalid value ''{0}'' for parameter ''{1}''. It is below the allowed minimum of {2}", value, parameterName, min),
                "INVALID_PARAMETER_VALUE");
    }
}
