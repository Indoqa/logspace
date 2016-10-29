/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api;

import java.text.MessageFormat;

public final class ParameterValueException extends AbstractLogspaceResourceException {

    private static final long serialVersionUID = 1L;

    private ParameterValueException(String message, String type) {
        super(message, HttpStatusCode.BadRequest, type);
    }

    private ParameterValueException(String message, String type, Throwable cause) {
        super(message, HttpStatusCode.BadRequest, type, cause);
    }

    public static ParameterValueException invalidValue(String reason, String message) {
        ParameterValueException result = new ParameterValueException(message, "INVALID_PARAMETER_VALUE");
        result.setParameter("reason", reason);
        return result;
    }

    public static ParameterValueException missingQueryParameter(String parameterName) {
        return new ParameterValueException(MessageFormat.format("Missing query parameter ''{0}''.", parameterName),
            "MISSING_QUERY_PARAMETER_VALUE");
    }

    public static ParameterValueException unparsableValue(String parameterName, String value, Throwable cause) {
        return new ParameterValueException(
            MessageFormat.format("Could not parse ''{0}'' as value for parameter ''{1}''.", value, parameterName),
            "UNPARSABLE_PARAMETER_VALUE", cause);
    }

    public static ParameterValueException valueTooLarge(String parameterName, Object value, Object max) {
        return new ParameterValueException(MessageFormat
            .format("Invalid value ''{0}'' for parameter ''{1}''. It is above the allowed maximum of {2}", value, parameterName, max),
            "INVALID_PARAMETER_VALUE");
    }

    public static ParameterValueException valueTooSmall(String parameterName, Object value, Object min) {
        return new ParameterValueException(MessageFormat
            .format("Invalid value ''{0}'' for parameter ''{1}''. It is below the allowed minimum of {2}", value, parameterName, min),
            "INVALID_PARAMETER_VALUE");
    }
}
