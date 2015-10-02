/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.logspace.hq.core.api.event.DataDefinition;

public class DataQuery implements Iterable<DataDefinition> {

    private List<DataDefinition> dataDefinitions = new ArrayList<DataDefinition>();

    public List<DataDefinition> getDataDefinitions() {
        return this.dataDefinitions;
    }

    @Override
    public Iterator<DataDefinition> iterator() {
        return this.dataDefinitions.iterator();
    }

    public void setDataDefinitions(List<DataDefinition> dataDefinitions) {
        this.dataDefinitions = dataDefinitions;
    }
}
