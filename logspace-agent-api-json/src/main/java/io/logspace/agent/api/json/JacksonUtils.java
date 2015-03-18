/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import io.logspace.agent.api.event.Optional;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public final class JacksonUtils {

    private static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String TIMEZONE_UTC = "UTC";

    private JacksonUtils() {
        // hide utility class constructor
    }

    public static void advance(JsonParser parser) throws IOException {
        consumeToken(parser);
        prepareToken(parser);
    }

    public static void consumeToken(JsonParser parser) {
        parser.clearCurrentToken();
    }

    public static String getFieldValue(JsonParser parser, String fieldName) throws IOException {
        validateTokenType(parser.nextToken(), FIELD_NAME);
        validateFieldName(parser.getCurrentName(), fieldName);

        return parser.nextTextValue();
    }

    public static void prepareToken(JsonParser parser) throws IOException {
        if (parser.getCurrentToken() == null) {
            parser.nextToken();
        }
    }

    public static Date readMandatoryDateField(JsonParser jsonParser, String fieldName) throws IOException {
        String value = readMandatoryField(jsonParser, fieldName);
        if (value == null) {
            return null;
        }

        try {
            return getTimeFormat().parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Expected date field of format '" + ISO_8601_DATE_FORMAT + "', but value '" + value
                    + "'.");
        }
    }

    public static String readMandatoryField(JsonParser parser, String fieldName) throws IOException {
        prepareToken(parser);

        validateTokenType(parser.getCurrentToken(), FIELD_NAME);
        validateFieldName(parser.getCurrentName(), fieldName);

        String result = parser.nextTextValue();
        consumeToken(parser);
        return result;
    }

    public static Optional<String> readOptionalField(JsonParser parser, String fieldName) throws IOException {
        prepareToken(parser);

        validateTokenType(parser.getCurrentToken(), FIELD_NAME);
        if (!parser.getCurrentName().equals(fieldName)) {
            return Optional.empty();
        }

        String result = parser.nextTextValue();
        consumeToken(parser);
        return Optional.of(result);
    }

    public static void validateFieldName(String fieldName, String expectedFieldName) {
        if (!fieldName.equals(expectedFieldName)) {
            throw new IllegalArgumentException("Expected field of name '" + expectedFieldName + "', but found field of name '"
                    + fieldName + "'.");
        }
    }

    public static void validateTokenType(JsonToken token, JsonToken... expected) {
        for (JsonToken eachExpected : expected) {
            if (token == eachExpected) {
                return;
            }
        }

        if (expected.length == 1) {
            throw new IllegalArgumentException("Expected token of type " + expected[0] + ", but found " + token);
        }

        throw new IllegalArgumentException("Expected token of type " + Arrays.toString(expected) + ", but found " + token);
    }

    public static void writeMandatoryDateField(JsonGenerator generator, String fieldName, Date value) throws IOException {
        String formattedValue;
        if (value == null) {
            formattedValue = null;
        } else {
            formattedValue = getTimeFormat().format(value);
        }

        writeMandatoryField(generator, fieldName, formattedValue);
    }

    public static void writeMandatoryField(JsonGenerator generator, String fieldName, String value) throws IOException {
        generator.writeStringField(fieldName, value);
    }

    public static void writeOptionalField(JsonGenerator generator, String fieldName, Optional<String> optional) throws IOException {
        if (optional.isPresent()) {
            writeMandatoryField(generator, fieldName, optional.get());
        }
    }

    private static DateFormat getTimeFormat() {
        DateFormat df = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
        return df;
    }
}