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
 * The Event is the core part of the logspace domain:
 * <ul>
 * <li>they must have a globally unique identifier,</li>
 * <li>they must have a timestamp</li>
 * <li>they can have a type,</li>
 * <li>they can reference other Events
 * <ul>
 * <li>a global Event which is the root cause for other Events</li>
 * <li>a parent Event which is the direct predecessor Event</li>
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
    String FIELD_MARKER = "marker";
    String FIELD_BOOLEAN_PROPERTIES = "boolean-properties";
    String FIELD_DATE_PROPERTIES = "date-properties";
    String FIELD_DOUBLE_PROPERTIES = "double-properties";
    String FIELD_FLOAT_PROPERTIES = "float-properties";
    String FIELD_INTEGER_PROPERTIES = "integer-properties";
    String FIELD_LONG_PROPERTIES = "long-properties";
    String FIELD_STRING_PROPERTIES = "string-properties";

    /**
     * @return The ID of the Agent which produced this Event.
     */
    String getAgentId();

    /**
     * @return The properties carrying Booleans. If there are no properties, the collection is empty.
     */
    Collection<BooleanEventProperty> getBooleanProperties();

    /**
     * @return The properties carrying Dates. If there are no properties, the collection is empty.
     */
    Collection<DateEventProperty> getDateProperties();

    /**
     * @return The properties carrying Doubles. If there are no properties, the collection is empty.
     */
    Collection<DoubleEventProperty> getDoubleProperties();

    /**
     * @return The properties carrying Floats. If there are no properties, the collection is empty.
     */
    Collection<FloatEventProperty> getFloatProperties();

    /**
     * @return The optional global Event ID.
     */
    String getGlobalEventId();

    /**
     * @return The id.
     */
    String getId();

    /**
     * @return The properties carrying Integers. If there are no properties, the collection is empty.
     */
    Collection<IntegerEventProperty> getIntegerProperties();

    /**
     * @return The properties carrying Longs. If there are no properties, the collection is empty.
     */
    Collection<LongEventProperty> getLongProperties();

    /**
     * @return The optional marker of the Event.
     */
    String getMarker();

    /**
     * @return The optional parent Event ID.
     */
    String getParentEventId();

    /**
     * @return The properties carrying Strings. If there are no properties, the collection is empty.
     */
    Collection<StringEventProperty> getStringProperties();

    /**
     * @return The system of the Event.
     */
    String getSystem();

    /**
     * @return The timestamp of the creation time.
     */
    Date getTimestamp();

    /**
     * @return The optional type of the Event.
     */
    String getType();

    /**
     * @return A boolean indicating if this Event has properties.
     */
    boolean hasProperties();

}
