/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonToken;

import io.logspace.agent.api.event.Optional;

/**
 * Base class for JSON deserializers. Simplifies handling of the {@link JsonParser}.
 */
public abstract class AbstractJsonDeserializer {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private JsonParser jsonParser;

    protected void advance() throws IOException {
        this.jsonParser.nextToken();
    }

    protected void consumeToken() {
        JacksonUtils.consumeToken(this.jsonParser);
    }

    protected String getCurrentName() throws IOException {
        return this.jsonParser.getCurrentName();
    }

    protected JsonParser getJsonParser() {
        return this.jsonParser;
    }

    protected boolean hasField(String fieldName) throws IOException {
        return fieldName.equals(this.jsonParser.getCurrentName());
    }

    protected boolean hasToken(JsonToken expected) {
        return this.jsonParser.getCurrentToken() == expected;
    }

    protected Boolean nextBooleanValue() throws IOException {
        return this.jsonParser.nextBooleanValue();
    }

    protected double nextDoubleValue() throws IOException {
        if (this.jsonParser.nextToken() == JsonToken.VALUE_NUMBER_FLOAT) {
            return this.jsonParser.getDoubleValue();
        }

        return 0;
    }

    protected int nextIntValue() throws IOException {
        return this.jsonParser.nextIntValue(0);
    }

    protected String nextTextValue() throws IOException {
        return this.jsonParser.nextTextValue();
    }

    protected void nextValue() throws IOException {
        this.jsonParser.nextValue();
    }

    protected void prepareToken() throws IOException {
        JacksonUtils.prepareToken(this.jsonParser);
    }

    protected Date readMandatoryDateField(String fieldName) throws IOException {
        return JacksonUtils.readMandatoryDateField(this.jsonParser, fieldName);
    }

    protected String readMandatoryField(String fieldName) throws IOException {
        return JacksonUtils.readMandatoryField(this.jsonParser, fieldName);
    }

    protected Long readMandatoryLongField(String fieldName) throws IOException {
        return JacksonUtils.readMandatoryLongField(this.jsonParser, fieldName);
    }

    protected Optional<String> readOptionalField(String fieldName) throws IOException {
        return JacksonUtils.readOptionalField(this.jsonParser, fieldName);
    }

    protected Optional<Integer> readOptionalIntField(String fieldName) throws IOException {
        return JacksonUtils.readOptionalIntField(this.jsonParser, fieldName);
    }

    protected String readString() throws IOException {
        return this.jsonParser.getText();
    }

    protected void setData(byte[] data) throws IOException {
        this.setJsonParser(JSON_FACTORY.createParser(data));
    }

    protected void setInputStream(InputStream inputStream) throws IOException {
        this.setJsonParser(JSON_FACTORY.createParser(inputStream));
    }

    protected void setJsonParser(JsonParser jsonParser) {
        this.jsonParser = jsonParser;
        this.jsonParser.configure(Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
    }

    protected void validateEnd() {
        this.validateTokenType((JsonToken) null);
    }

    protected void validateFieldName(String... expected) throws IOException {
        JacksonUtils.validateFieldName(this.jsonParser.getCurrentName(), expected);
        this.jsonParser.nextToken();
    }

    protected void validateTokenType(JsonToken... expected) {
        JacksonUtils.validateTokenType(this.jsonParser.getCurrentToken(), expected);
    }
}
