package io.logspace.passive.agent.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * Immutable event object which carries explicit basic information (id, timestamp, type, globalEventId, parentEventId)
 * and a collection of key-value pairs with the details.
 */
public final class Event {

    /**
     * A unique identifier of the event.
     */
    private final String id;

    /**
     * The event's creation time.
     */
    private final Date timestamp;

    /**
     * The optional type of the event indicating what properties are expected.
     */
    private final Optional<String> type;

    /**
     * A optional global event is the root for multiple sub events.
     */
    private Optional<String> globalEventId;

    /**
     * The optional direct predecessor event related to this event.
     */
    private Optional<String> parentEventId;

    /**
     * Key-value pairs of additional information.
     */
    private final Collection<EventProperty> properties;

    /**
     * Create an new event: the <code>id</code> and the <code>timestamp</code> are set automatically by using
     * {@link UUID#randomUUID()} and <code>new {@link Date}</code>.
     * 
     * @param type The event type.
     * @param globalEventId The global event id.
     * @param parentEventId The parent event id.
     * @param properties The properties.
     */
    public Event(Optional<String> type, Optional<String> globalEventId, Optional<String> parentEventId, EventProperty... properties) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = new Date();

        this.type = type;
        this.globalEventId = globalEventId;
        this.parentEventId = parentEventId;
        this.properties = Arrays.asList(properties);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Event other = (Event) obj;
        return this.id.equals(other.id);
    }

    /**
     * @return The optional global event id.
     */
    public Optional<String> getGlobalEventId() {
        return this.globalEventId;
    }

    /**
     * @return The id.
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return The optional parent event id.
     */
    public Optional<String> getParentEventId() {
        return this.parentEventId;
    }

    /**
     * @return A collection of properties. If there are no properties, the collection is empty.
     */
    public Collection<EventProperty> getProperties() {
        return new ArrayList<EventProperty>(this.properties);
    }

    /**
     * @return The timestamp of the creation time.
     */
    public Date getTimestamp() {
        return this.timestamp;
    }

    /**
     * @return The optional type of the event.
     */
    public Optional<String> getType() {
        return this.type;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
