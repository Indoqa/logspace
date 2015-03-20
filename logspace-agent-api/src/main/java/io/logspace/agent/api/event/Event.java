/**
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package io.logspace.agent.api.event;

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

    public static final String FIELD_ID = "id";
    public static final String FIELD_TIMESTAMP = "timestamp";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_PARENT_EVENT_ID = "pid";
    public static final String FIELD_GLOBAL_EVENT_ID = "gid";
    public static final String FIELD_PROPERTIES = "properties";

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

    /**
     * @return A boolean indicating if this event has properties.
     */
    boolean hasProperties();

}
