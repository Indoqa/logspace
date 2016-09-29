/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.core.api.nativequery;

import java.util.Map;

import io.logspace.hq.core.api.event.NativeQueryResult;

public interface NativeQueryService {

    /**
     * Execute a query directly against the underlying Event Store using the given parameters.
     *
     * @param parameters The parameters to be used for the query.
     *
     * @return A {@link NativeQueryResult} with the result.
     */
    NativeQueryResult executeNativeQuery(Map<String, String[]> parameters);
}
