package io.logspace.hq.core.api.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventFilter implements Iterable<EventFilterElement> {

    private List<EventFilterElement> elements = new ArrayList<EventFilterElement>();

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
