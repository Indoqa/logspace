---
layout: default
title: Getting Started
---

# Getting Started
>The source code of this tutorial is available at https://github.com/Indoqa/logspace-sample/

The goal of this tutorial is adding Logspace to a Java application. It explains

- which libraries you need at compile time,
- how to write a simple agent that produces events,
- which libraries you need at runtime
- and how to provide the necessary configuration file (logspace.json) to establish the communication with the Logspace HQ (headquarters) which is the receiver of all events.

Also read the [System Overview](/system-overview) to get familier with the Logspace architecture and its components.

## Compile time dependencies
Implementing a Logspace agent only requires adding the logspace-agent-api to your classpath:

````xml
<dependency>
  <groupId>io.logspace</groupId>
  <artifactId>logspace-agent-api</artifactId>
  <version>{{site.data.logspace.version}}</version>
</dependency>
````

It contains all the necessary interfaces and abstract implementations to write your own agent. It only has one dependency on slf4j-api. The Logspace Agent API requires Java 6 or higher.

## A simple agent
Generally there are two types of agents:

- The ApplicationAgent is triggered by the application. It is useful if the application "knows" when an event should be produces.
- The SchedulerAgent is triggered by a scheduler. It is useful if producing an event should be controlled on a time-based manner.

In order to keep this tutorial simple, an ApplicationAgent will be implemented. Using it from within your application code is as simple as using a logging framework:

````java
public final class DateTimeService {

    private UsageAgent usageAgent = new UsageAgent();

    public String getDateTime(String timezone) {
        long start = System.nanoTime();
        try {
            return this.getDateTimeAsString(timezone);
        } finally {
            this.usageAgent.logDateTimeUsage(timezone, System.nanoTime() - start);
        }
    }

    // check for available timezones
    private String getDateTimeAsString(String requestedTimezone) {
        ....
    }
}
````

And here is the agent implementation:

````java
package io.logspace.sample.service;

import io.logspace.agent.api.AbstractApplicationAgent;
import io.logspace.agent.api.event.Event;

public class UsageAgent extends AbstractApplicationAgent {

    public UsageAgent() {
        super("usage-agent", "usage");
    }

    public void logDateTimeUsage(String timezone, long serviceResponseTime) {
        Event event = new DateTimeUsageEventBuilder(this.getId(), this.getSystem())
            .addTimeZone(timezone)
            .addServiceResponseTime(serviceResponseTime)
            .toEvent();
        this.sendEvent(event);
    }
}
````

It is recommended to use the AbstractApplicationAgent because it take care of establishing the communication with the *agent controller*. The agent controller collects all events within your application.

A Logspace agent packages all information into *Events*. An *Event* is defined by the interface ```io.logspace.agent.api.event.Event```. This interface defines the general attributes of all *Events*, like an ID and a timestamp.

Since some of those attributes are optional and others are mandatory, Logspace relies on the [Builder Pattern] (http://en.wikipedia.org/wiki/Builder_pattern) to create *Events*. The best way to create your own *Events* is to create your own *EventBuilder* by extending the class ```io.logspace.agent.api.event.AbstractEventBuilder```.

Using the ```DateTimeUsageEventBuilder``` will result in an *Event*.<br/>
It will carry the defined **Type** but it is still lacking any specific information.

In order to add specific information to *Events* Logspace uses *EventProperties*, which are defined by the interface ```io.logspace.agent.api.event.EventProperty```.
There are specific *EventProperties* that can carry different types of data: ```LongEventProperty```, ```StringEventProperty```, ```DateEventProperty```, etc.

The best way to add *EventProperties* to your *Events* is to add specific methods to your *EventBuilder* that will generate the appropriate *EventProperty*. (see ```addTimeZone(timezone)```).

````java
package io.logspace.sample.service;

import io.logspace.agent.api.event.AbstractEventBuilder;
import io.logspace.agent.api.event.Optional;

/*default*/ class DateTimeUsageEventBuilder extends AbstractEventBuilder {

    private static final String TYPE = "date-time";

    private static final String PROPERTY_SERVICE_RESPONSE_TIME = "response_time";
    private static final String PROPERTY_TIMEZONE = "timezone";

    private Optional<String> type = Optional.of(TYPE);

    public DateTimeUsageEventBuilder(String agentId, String system) {
        super(agentId, system);
    }

    public DateTimeUsageEventBuilder addServiceResponseTime(final long responseTime) {
        this.addProperty(PROPERTY_SERVICE_RESPONSE_TIME, responseTime);
        return this;
    }

    public DateTimeUsageEventBuilder addTimeZone(final String timezone) {
        this.addProperty(PROPERTY_TIMEZONE, timezone);
        return this;
    }

    @Override
    protected Optional<String> getType() {
        return this.type;
    }
}
````

## Provide a headquarters agent controller as runtime depdendency
The frequently used agent controller is the *HQAgentController*. In order to add it to your application, (only) one runtime dependency is needed:

````xml
<dependency>
  <groupId>io.logspace</groupId>
  <artifactId>logspace-agent-controller</artifactId>
  <version>{{site.data.logspace.version}}</version>
  <scope>runtime</scope>
</dependency>
````

This library has of course a dependency on the *logspace-agent-api* and also requires *slf4j-api*.

> Logspace uses a [Shaded JAR](https://maven.apache.org/plugins/maven-shade-plugin/) for its own libraries, so conflicts with your libraries should be extremely rare.

As explained above the agent detects its controller automatically if it extends the *AbstractApplicationAgent*.

## Configure your agent controller
The lookup mechanism for the headquarters agent expects the configuration in the root of your classpath and searches the file *logspace.json*:

````javascript
{
  "id" : "logspace-sample",
  "class-name" : "io.logspace.agent.hq.HqAgentController",
  "parameters" : {
    "space-token" : "8kn3GGXvVVyeeE2m2eyu",
    "base-url" : "http://localhost:4567",
    "queue-directory" : "./target/"
  }
}
````

The *id* has to be a unique identifier for your agent controller. E.g. it is used at the headquarters to provide a configuration.
The *class-name* references the implementation. The most common use case is the *HQAgentController*.
The parameter *space-token* is used for authentication so that only authorized agents can send events to the headquarters.
The parameter *base-url* is used to establish a HTTP connection to the headquarters.
The parameter *queue-directory* is a file system path. It is used to buffer events and to enable retries in the case the headquarters cannot be reached.

## That's it!
If you want to test your first Logspace agent, start the Logspace headquarters in demo mode. The configuration above will work with a locally running Logspace headquarters. The [Logspace Demo](/demo) step 2 explains how to run LogspaceHQ in demo mode.
