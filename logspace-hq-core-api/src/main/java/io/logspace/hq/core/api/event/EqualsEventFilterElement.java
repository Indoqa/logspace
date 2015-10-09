package io.logspace.hq.core.api.event;

public class EqualsEventFilterElement extends AbstractEventFilterElement {

    private Object value;

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
