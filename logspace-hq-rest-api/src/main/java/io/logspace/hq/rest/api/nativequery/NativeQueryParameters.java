/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.nativequery;

import java.util.HashMap;
import java.util.Map;

public class NativeQueryParameters {

    private Map<String, String[]> parameters = new HashMap<>();

    public void add(String parameterName, String value) {
        String[] oldValue = this.parameters.get(parameterName);
        if (oldValue == null) {
            this.parameters.put(parameterName, new String[] {value});
            return;
        }

        String[] newValue = new String[oldValue.length + 1];
        System.arraycopy(oldValue, 0, newValue, 0, oldValue.length);
        newValue[oldValue.length] = value;
        this.parameters.put(parameterName, newValue);
    }

    public Map<String, String[]> getParameters() {
        return this.parameters;
    }

    public void setParameters(Map<String, String[]> parameters) {
        this.parameters = parameters;
    }
}
