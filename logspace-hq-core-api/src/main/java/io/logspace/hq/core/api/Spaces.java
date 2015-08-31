/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api;

/**
 * Spaces are used to distinguish events from different sources.<br>
 * To ensure some basic authentication for a space, authenticationTokens must be configured for a space, which consumers must supply as
 * an authenticationToken to store their events.
 */
public interface Spaces {

    /**
     * Retrieve the space for the supplied authenticationToken.
     *
     * @param authenticationToken - The supplied authenticationToken.
     * @return The space for the authenticationToken or <code>null</code> if no space is configured for this token.
     */
    String getSpaceForAuthenticationToken(String authenticationToken);

}
