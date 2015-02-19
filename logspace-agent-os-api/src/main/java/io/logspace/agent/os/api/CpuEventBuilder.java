package io.logspace.agent.os.api;

import io.logspace.agent.api.event.AbstractEventBuilder;
import io.logspace.agent.api.event.Event;
import io.logspace.agent.api.event.EventProperty;
import io.logspace.agent.api.event.Optional;

public class CpuEventBuilder extends AbstractEventBuilder {

    public static final String TYPE = "os-cpu";
    public static final String PROPERTY_LOAD_AVERAGE = "load-average";

    public CpuEventBuilder() {
        super();
    }

    public CpuEventBuilder(Event parentEvent) {
        super(parentEvent);
    }

    /**
     * @param loadAverage The value of the average load.
     * @return This event builder.
     */
    public CpuEventBuilder setLoadAverage(double loadAverage) {
        this.add(new EventProperty(PROPERTY_LOAD_AVERAGE, Double.toString(loadAverage)));
        return this;
    }

    /**
     * @see io.logspace.agent.api.event.AbstractEventBuilder#getType()
     */
    @Override
    protected Optional<String> getType() {
        return Optional.of(TYPE);
    }
}
