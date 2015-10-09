package io.logspace.hq.core.api.event;

public class RangeEventFilterElement extends AbstractEventFilterElement {

    private Object from;
    private Object to;

    public Object getFrom() {
        return this.from;
    }

    public Object getTo() {
        return this.to;
    }

    public void setFrom(Object from) {
        this.from = from;
    }

    public void setTo(Object to) {
        this.to = to;
    }
}
