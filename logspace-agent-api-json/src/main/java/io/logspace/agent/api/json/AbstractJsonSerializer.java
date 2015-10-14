/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import io.logspace.agent.api.event.Optional;

/**
 * Base class for JSON serializers. Simplifies handling of the {@link JsonGenerator}.
 */
public abstract class AbstractJsonSerializer {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private final Logger logger = LoggerFactory.getLogger(EventJsonSerializer.class);

    private JsonGenerator jsonGenerator;

    protected AbstractJsonSerializer(JsonGenerator jsonGenerator) {
        this.jsonGenerator = jsonGenerator;
    }

    protected AbstractJsonSerializer(OutputStream outputStream) throws IOException {
        super();

        this.jsonGenerator = this.createJsonGenerator(outputStream);
    }

    protected void endArray() throws IOException {
        this.jsonGenerator.writeEndArray();
    }

    protected void endObject() throws IOException {
        this.jsonGenerator.writeEndObject();
    }

    protected void finish() throws IOException {
        this.jsonGenerator.flush();
        this.jsonGenerator.close();
    }

    protected JsonGenerator getJsonGenerator() {
        return this.jsonGenerator;
    }

    protected void startArray() throws IOException {
        this.jsonGenerator.writeStartArray();
    }

    protected void startObject() throws IOException {
        this.jsonGenerator.writeStartObject();
    }

    protected void writeField(String fieldName) throws IOException {
        this.jsonGenerator.writeFieldName(fieldName);
    }

    protected void writeMandatoryDateField(String fieldName, Date value) throws IOException {
        JacksonUtils.writeMandatoryDateField(this.jsonGenerator, fieldName, value);
    }

    protected void writeMandatoryDoubleField(String fieldName, double value) throws IOException {
        JacksonUtils.writeMandatoryDoubleField(this.jsonGenerator, fieldName, value);
    }

    protected void writeMandatoryLongField(String fieldName, long value) throws IOException {
        JacksonUtils.writeMandatoryLongField(this.jsonGenerator, fieldName, value);
    }

    protected void writeMandatoryStringField(String fieldName, String value) throws IOException {
        JacksonUtils.writeMandatoryStringField(this.jsonGenerator, fieldName, value);
    }

    protected void writeOptionalField(String fieldName, Optional<String> value) throws IOException {
        JacksonUtils.writeOptionalField(this.jsonGenerator, fieldName, value);
    }

    protected void writeOptionalIntField(String fieldName, Optional<Integer> value) throws IOException {
        JacksonUtils.writeOptionalIntField(this.jsonGenerator, fieldName, value);
    }

    protected void writeString(String value) throws IOException {
        this.jsonGenerator.writeString(value);
    }

    private JsonGenerator createJsonGenerator(OutputStream baos) throws IOException {
        JsonGenerator result = JSON_FACTORY.createGenerator(baos, UTF8);

        result.configure(Feature.QUOTE_NON_NUMERIC_NUMBERS, false);
        result.configure(Feature.AUTO_CLOSE_TARGET, false);

        if (this.logger.isDebugEnabled()) {
            result.setPrettyPrinter(new DefaultPrettyPrinter());
        }

        return result;
    }
}
