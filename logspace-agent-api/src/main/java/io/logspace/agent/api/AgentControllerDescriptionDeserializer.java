/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface defining a deserializer for {@link AgentControllerDescription AgentControllerDescriptions}.
 */
public interface AgentControllerDescriptionDeserializer {

    /**
     * @param inputStream - The inputStream to read from.
     *
     * @throws IOException If an error occurs.
     *
     * @return The {@link AgentControllerDescription}
     */
    AgentControllerDescription read(InputStream inputStream) throws IOException;

}
