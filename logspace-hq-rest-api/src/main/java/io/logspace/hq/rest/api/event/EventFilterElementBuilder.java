/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.event;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.logspace.hq.rest.api.event.MultiValueEventFilterElement.Operator;

public class EventFilterElementBuilder {

    private Operator operator;
    private String property;
    private String value;
    private Object to;
    private Object from;
    private List<String> values;

    public EventFilterElement build() throws JsonProcessingException {
        if (this.value != null) {
            return EqualsEventFilterElement.create(this.property, this.value);
        }

        if (this.from != null || this.to != null) {
            return RangeEventFilterElement.create(this.property, this.from, this.to);
        }

        if (this.values != null) {
            if (this.operator == null) {
                throw new JsonMappingException(
                    "No operator was supplied for MultiValueEventFilterElement. The following operators are supported: "
                            + Arrays.asList(Operator.values()));
            }
            return MultiValueEventFilterElement.create(this.property, this.operator, this.values);
        }

        return null;
    }

    public void withFrom(Object fromParameter) {
        this.from = fromParameter;
    }

    public void withOperator(Operator operatorParameter) {
        this.operator = operatorParameter;
    }

    public void withProperty(String propertyParameter) {
        this.property = propertyParameter;
    }

    public void withTo(Object toParameter) {
        this.to = toParameter;
    }

    public void withValue(String valueParameter) {
        this.value = valueParameter;
    }

    public void withValues(List<String> valuesParameter) {
        this.values = valuesParameter;
    }
}
