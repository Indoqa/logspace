package io.logspace.agent.api.event;


/**
 * Default event builder implementation which sets an empty type.
 */
public class DefaultEventBuilder extends AbstractEventBuilder {

    @Override
    protected Optional<String> getType() {
        return Optional.empty();
    }
}
