/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import io.logspace.agent.api.event.Optional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public abstract class AbstractJsonDeserializer {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private final JsonParser jsonParser;

    protected AbstractJsonDeserializer(byte[] data) throws IOException {
        this.jsonParser = JSON_FACTORY.createParser(data);
    }

    protected AbstractJsonDeserializer(InputStream inputStream) throws IOException {
        this.jsonParser = JSON_FACTORY.createParser(inputStream);
    }

    protected void consumeToken() {
        JacksonUtils.consumeToken(this.jsonParser);
    }

    protected String getCurrentName() throws IOException {
        return this.jsonParser.getCurrentName();
    }

    protected boolean hasToken(JsonToken expected) {
        return this.jsonParser.getCurrentToken() == expected;
    }

    protected String nextTextValue() throws IOException {
        return this.jsonParser.nextTextValue();
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

    protected Optional<String> readOptionalField(String fieldName) throws IOException {
        return JacksonUtils.readOptionalField(this.jsonParser, fieldName);
    }

    protected void validateEnd() {
        this.validateToken((JsonToken) null);
    }

    protected void validateField(String fieldName) throws IOException {
        JacksonUtils.validateFieldName(this.jsonParser.getCurrentName(), fieldName);
        this.jsonParser.nextToken();
    }

    protected void validateToken(JsonToken... expected) {
        JacksonUtils.validateTokenType(this.jsonParser.getCurrentToken(), expected);
    }
}