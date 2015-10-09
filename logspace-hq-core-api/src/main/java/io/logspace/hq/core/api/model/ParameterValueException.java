package io.logspace.hq.core.api.model;

import java.text.MessageFormat;

import io.logspace.agent.api.HttpStatusCode;

public class ParameterValueException extends AbstractLogspaceResourceException {

    private static final long serialVersionUID = 1L;

    private ParameterValueException(String message, String TYPE) {
        super(message, HttpStatusCode.BadRequest, TYPE);
    }

    public static ParameterValueException unparsableValue(String parameterName, String value) {
        return new ParameterValueException(
            MessageFormat.format("Could not parse ''{0}'' as value for parameter ''{1}''.", value, parameterName),
            "UNPARSABLE_PARAMETER_VALUE");
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
