package io.logspace.hq.core.api.event;

public abstract class AbstractEventFilterElement implements EventFilterElement {

    private String propertyName;

    @Override
    public String getPropertyName() {
        return this.propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
