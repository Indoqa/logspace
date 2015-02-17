package io.logspace.passive.agent.api.event;

import java.util.Collection;
import java.util.Date;

/**
 * The event is the core part of the logspace domain:
 * <ul>
 * <li>they must have a globally unique identifier,</li>
 * <li>they must have a timestamp</li>
 * <li>they can have a type,</li>
 * <li>they can reference other events
 * <ul>
 * <li>a global event which is the root cause for other events</li>
 * <li>a parent event which is the direct predecessor event</li>
 * </ul>
 * <li>and the can carry any additional information in the form of a collection of key-value pairs.
 * </ul>
 */
public interface Event {

    /**
     * @return The optional global event id.
     */
    Optional<String> getGlobalEventId();

    /**
     * @return The id.
     */
    String getId();

    /**
     * @return The optional parent event id.
     */
    Optional<String> getParentEventId();

    /**
     * @return A collection of properties. If there are no properties, the collection is empty.
     */
    Collection<EventProperty> getProperties();

    /**
     * @return The timestamp of the creation time.
     */
    Date getTimestamp();

    /**
     * @return The optional type of the event.
     */
    Optional<String> getType();

}
