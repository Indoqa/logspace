/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;
import static io.logspace.agent.api.AgentControllerDescription.FIELD_CLASS_NAME;
import static io.logspace.agent.api.AgentControllerDescription.FIELD_ID;
import static io.logspace.agent.api.AgentControllerDescription.FIELD_PARAMETERS;
import io.logspace.agent.api.AgentControllerDescription;
import io.logspace.agent.api.AgentControllerDescription.Parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class AgentControllerDescriptionJsonSerializer extends AbstractJsonSerializer {

    private AgentControllerDescriptionJsonSerializer(OutputStream outputStream) throws IOException {
        super(outputStream);
    }

    public static String toJson(AgentControllerDescription description) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        toJson(description, baos);

        return baos.toString(UTF8.getJavaName());
    }

    public static void toJson(AgentControllerDescription description, OutputStream outputStream) throws IOException {
        new AgentControllerDescriptionJsonSerializer(outputStream).serialize(description);
    }

    private void serialize(AgentControllerDescription description) throws IOException {
        this.startObject();

        this.writeAttributes(description);
        this.writeParameters(description);

        this.endObject();

        this.finish();
    }

    private void writeAttributes(AgentControllerDescription description) throws IOException {
        this.writeMandatoryStringField(FIELD_ID, description.getId());
        this.writeMandatoryStringField(FIELD_CLASS_NAME, description.getClassName());
    }

    private void writeParameters(AgentControllerDescription description) throws IOException {
        if (description.getParameterCount() == 0) {
            return;
        }

        this.writeFieldName(FIELD_PARAMETERS);

        this.startObject();

        for (Parameter eachParameter : description.getParameters()) {
            this.writeMandatoryStringField(eachParameter.getName(), eachParameter.getValue());
        }

        this.endObject();
    }
}
