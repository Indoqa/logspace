---
layout: default
title: Getting Started
---

# Getting Started
>The source code of this tutorial is available at https://github.com/Indoqa/logspace-sample/

The goal of this tutorial is adding Logspace to a Java application. It explains

- which libraries are needed at compile time,
- how to write a simple *Agent* that produces events,
- which libraries are needed at runtime
- and how to provide the necessary configuration to establish the communication with the Logspace HQ.

Also read the [System Overview](/system-overview) to get familiar with the Logspace architecture and its components.

## Compile Time Dependencies
Implementing a Logspace agent only requires adding the **logspace-agent-api** to your classpath:

````xml
<dependency>
  <groupId>io.logspace</groupId>
  <artifactId>logspace-agent-api</artifactId>
  <version>{{site.data.logspace.version}}</version>
</dependency>
````

It contains all the necessary interfaces and abstract implementations to write your own agent. It only has one dependency on **slf4j-api** and works with Java 6 or higher.

## A Simple Agent
Generally there are two types of agents:

- The *ApplicationAgent* is triggered by the application itself.<br/>This type of *Agent* is to be used when the application "knows" when an *Event* is to be produced.
- The *SchedulerAgent* is triggered by a scheduler.<br/>This type of Agent is to be used when *Events* should be produced in a time-based manner.

In this tutorial an *ApplicationAgent* will be implemented. Using it from within your application code is as simple as using a logging framework:

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

    private String getDateTimeAsString(String requestedTimezone) {
        ....
    }
}
````

And here is the *Agent* implementation:

````java
package io.logspace.sample.service;

import io.logspace.agent.api.AbstractApplicationAgent;
import io.logspace.agent.api.event.Event;

public class UsageAgent extends AbstractApplicationAgent {

    public UsageAgent() {
        super("usage-agent", "usage");
    }

    public void logDateTimeUsage(String timezone, long serviceResponseTime) {
        Event event = new DateTimeUsageEventBuilder(this.getEventBuilderData())
            .addTimeZone(timezone)
            .addServiceResponseTime(serviceResponseTime)
            .toEvent();
        this.sendEvent(event);
    }
}
````

It is recommended to extend the ```AbstractApplicationAgent``` because it takes care of establishing the communication with the *AgentController*. The *AgentController* collects all *Events* within your application.

A Logspace *Agent* packages all information into *Events*. An *Event* is defined by the interface ```io.logspace.agent.api.event.Event```. This interface defines the general attributes of all *Events*, like an ID and a timestamp.

Since some of those attributes are optional and others are mandatory, Logspace relies on the [Builder Pattern] (http://en.wikipedia.org/wiki/Builder_pattern) to create *Events*. The best way to create your own *Events* is to create your own *EventBuilder* by extending the class ```io.logspace.agent.api.event.AbstractEventBuilder```.

In order to add specific information to *Events* Logspace uses *EventProperties*, which are defined by the interface ```io.logspace.agent.api.event.EventProperty```.
There are specific *EventProperties* that can carry different types of data: ```LongEventProperty```, ```StringEventProperty```, ```DateEventProperty```, etc.

The best way to add *EventProperties* to your *Events* is to add specific methods to your *EventBuilder* that will generate the appropriate *EventProperty*. (see ```addTimeZone(timezone)```).

````java
package io.logspace.sample.service;

import io.logspace.agent.api.event.AbstractEventBuilder;

/*default*/ class DateTimeUsageEventBuilder extends AbstractEventBuilder {

    private static final String TYPE = "date-time";

    private static final String PROPERTY_SERVICE_RESPONSE_TIME = "response_time";
    private static final String PROPERTY_TIMEZONE = "timezone";

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
    protected String getType() {
        return TYPE;
    }
}
````


## Runtime Dependencies
Logspace comes with several different implementations of *AgentController* that cover a wide range of use cases.
To use one of those *AgentControllers* you need the **logspace-agent-controller** in your runtime classpath:

````xml
<dependency>
  <groupId>io.logspace</groupId>
  <artifactId>logspace-agent-controller</artifactId>
  <version>{{site.data.logspace.version}}</version>
  <scope>runtime</scope>
</dependency>
````

This library has of course a dependency on the **logspace-agent-api** and also requires **slf4j-api**.

> Logspace uses a [Shaded JAR](https://maven.apache.org/plugins/maven-shade-plugin/) for its own libraries, so conflicts with your libraries should be extremely rare.&nbsp;

## Configuration
Create a configuration file called **logspace.json** and add it to your application's classpath.

The content of the file should look like this:

````json
{
  "id" : "logspace-sample",
  "class-name" : "io.logspace.agent.hq.HqAgentController",
  "parameters" : {
    "space-token" : "sample",
    "base-url" : "http://localhost:4567",
    "queue-directory" : "./target/"
  }
}
````

- The parameter **id** defines the identifier of your *AgentController*. It should be a unique value since it is used to distinguish between different controllers.
- The parameter **class-name** defines which implementation of *AgentController* to load. To upload *Events* to the *Logspace HQ* use the ```io.logspace.agent.hq.HqAgentController```.
- The parameter **space-token** is used for authentication so that only authorized *AgentControllers* can send *Events* to the *Logspace HQ*.
- The parameter **base-url** is used to establish a HTTP connection to the headquarters.
- The parameter **queue-directory** is a file system path. It is used to buffer events and to enable retries in case the *Logspace HQ* cannot be reached.

## That's it!
The configuration above will upload the *Events* of your *Agent* to the *Logspace HQ*.<br/>
You can use the HQ from [Step 2](/demo#step-2:-start-the-logspace-hq) of the Logspace Demo for this!
