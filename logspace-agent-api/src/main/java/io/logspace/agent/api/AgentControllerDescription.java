/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class AgentControllerDescription {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CLASS_NAME = "class-name";
    public static final String FIELD_PARAMETERS = "parameters";
    public static final String FIELD_PARAMETER_NAME = "parameter-name";
    public static final String FIELD_PARAMETER_VALUE = "parameter-value";

    private String className;

    private String id;

    private List<Parameter> parameters = new ArrayList<Parameter>();

    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }

    public void addParameter(String name, String value) {
        this.addParameter(Parameter.create(name, value));
    }

    public String getClassName() {
        return this.className;
    }

    public String getId() {
        return this.id;
    }

    public Parameter getParameter(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The parameter name must not be NULL.");
        }

        for (Parameter eachParameter : this.parameters) {
            if (name.equals(eachParameter.getName())) {
                return eachParameter;
            }
        }

        return null;
    }

    public int getParameterCount() {
        return this.parameters.size();
    }

    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public String getParameterValue(String name) {
        return this.getParameterValue(name, null);
    }

    public String getParameterValue(String name, String defaultValue) {
        Parameter parameter = this.getParameter(name);

        if (parameter == null) {
            return defaultValue;
        }

        String value = parameter.getValue();
        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public static class Parameter {

        private String name;
        private String value;

        public static Parameter create(String name, String value) {
            Parameter result = new Parameter();

            result.setName(name);
            result.setValue(value);

            return result;
        }

        public String getName() {
            return this.name;
        }

        public String getValue() {
            return this.value;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return MessageFormat.format("{0} = {1}", this.name, this.value);
        }
    }
}
