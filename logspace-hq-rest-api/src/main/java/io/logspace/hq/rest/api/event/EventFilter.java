/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.hq.rest.api.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventFilter implements Iterable<EventFilterElement> {

    private List<EventFilterElement> elements = new ArrayList<EventFilterElement>();

    public void add(EventFilterElement element) {
        this.elements.add(element);
    }

    public List<EventFilterElement> getElements() {
        return this.elements;
    }

    @Override
    public Iterator<EventFilterElement> iterator() {
        return this.elements.iterator();
    }

    public void setElements(List<EventFilterElement> elements) {
        this.elements = elements;
    }
}
