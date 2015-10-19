/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.event;

import java.util.ArrayList;
import java.util.List;

public class MultiValueEventFilterElement extends AbstractEventFilterElement {

    private List<String> values = new ArrayList<>();

    public static MultiValueEventFilterElement create(String property, List<String> values) {
        MultiValueEventFilterElement result = new MultiValueEventFilterElement();

        result.setPropertyName(property);
        result.setValues(values);

        return result;
    }

    public List<String> getValues() {
        return this.values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

}
