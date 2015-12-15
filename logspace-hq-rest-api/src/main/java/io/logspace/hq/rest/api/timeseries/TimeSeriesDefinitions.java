/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.timeseries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TimeSeriesDefinitions implements Iterable<TimeSeriesDefinition> {

    private List<TimeSeriesDefinition> definitions = new ArrayList<TimeSeriesDefinition>();

    public TimeSeriesDefinition getDefinition(int index) {
        return this.definitions.get(index);
    }

    public int getDefinitionCount() {
        return this.definitions.size();
    }

    public List<TimeSeriesDefinition> getDefinitions() {
        return this.definitions;
    }

    @Override
    public Iterator<TimeSeriesDefinition> iterator() {
        return this.definitions.iterator();
    }

    public void setDefinitions(List<TimeSeriesDefinition> definitions) {
        this.definitions = definitions;
    }
}
