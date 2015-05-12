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

    String FIELD_ID = "id";
    String FIELD_TIMESTAMP = "timestamp";
    String FIELD_TYPE = "type";
    String FIELD_SYSTEM = "system";
    String FIELD_AGENT_ID = "agent-id";
    String FIELD_PARENT_EVENT_ID = "pid";
    String FIELD_GLOBAL_EVENT_ID = "gid";
    String FIELD_BOOLEAN_PROPERTIES = "boolean-properties";
    String FIELD_DATE_PROPERTIES = "date-properties";
    String FIELD_DOUBLE_PROPERTIES = "double-properties";
    String FIELD_FLOAT_PROPERTIES = "float-properties";
    String FIELD_INTEGER_PROPERTIES = "integer-properties";
    String FIELD_LONG_PROPERTIES = "long-properties";
    String FIELD_STRING_PROPERTIES = "string-properties";

    /**
     * @return The optional global event ID.
     */
    Optional<String> getGlobalEventId();

    /**
     * @return The ID of the Agent which produced this event.
     */
    String getAgentId();

    /**
     * @return The id.
     */
    String getId();

    /**
     * @return The optional parent event ID.
     */
    Optional<String> getParentEventId();

    /**
     * @return The properties carrying Booleans. If there are no properties, the collection is empty.
     */
    Collection<BooleanEventProperty> getBooleanProperties();

    /**
     * @return The properties carrying Dates. If there are no properties, the collection is empty.
     */
    Collection<DateEventProperty> getDateProperties();

    /**
     * @return The properties carrying Integers. If there are no properties, the collection is empty.
     */
    Collection<IntegerEventProperty> getIntegerProperties();

    /**
     * @return The properties carrying Longs. If there are no properties, the collection is empty.
     */
    Collection<LongEventProperty> getLongProperties();

    /**
     * @return The properties carrying Floats. If there are no properties, the collection is empty.
     */
    Collection<FloatEventProperty> getFloatProperties();

    /**
     * @return The properties carrying Doubles. If there are no properties, the collection is empty.
     */
    Collection<DoubleEventProperty> getDoubleProperties();

    /**
     * @return The properties carrying Strings. If there are no properties, the collection is empty.
     */
    Collection<StringEventProperty> getStringProperties();

    /**
     * @return The timestamp of the creation time.
     */
    Date getTimestamp();

    /**
     * @return The optional type of the event.
     */
    Optional<String> getType();

    /**
     * @return The system of the event.
     */
    String getSystem();

    /**
     * @return A boolean indicating if this event has properties.
     */
    boolean hasProperties();

}