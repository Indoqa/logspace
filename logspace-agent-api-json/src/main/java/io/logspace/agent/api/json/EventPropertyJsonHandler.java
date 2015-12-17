/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.json;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import io.logspace.agent.api.event.EventProperties;
import io.logspace.agent.api.event.EventProperty;

public interface EventPropertyJsonHandler<T> {

    String getFieldName();

    void readEventProperties(EventProperties result, String propertyName, JsonParser jsonParser) throws IOException;

    void writeEventProperties(JsonGenerator jsonGenerator, Collection<? extends EventProperty<T>> properties) throws IOException;

}
