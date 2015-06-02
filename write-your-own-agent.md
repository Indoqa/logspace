---
layout: default
title: Write Your Own Agent
---

#Write Your Own Agent

If you cannot find an *Agent* that collects the information you need or want to track specific processes in your application you can extend logspace by writing your own *Agents*.


##Prerequisites

You should be familiar with the [architecture](/architecture) of logspace so you understand what your *Agent* is supposed to do and which component it is supposed to interact with.

In order to create your own *Agent* you need the **logspace-agent-api.jar** in your classpath.
It contains the interfaces defining *Agents*, their *Capabilities*, *Events*, and *Orders*.

If your *Agent* will require additional libraries to complete its task, you will need those libraries as well, of course.
logspace uses a [Shaded JAR](https://maven.apache.org/plugins/maven-shade-plugin/) for its own libraries, so conflicts with your libraries should be extremely rare.


##The Simplest Agent In The World

The easiest way to write an *Agent* is to extend the class **io.logspace.agent.api.AbstractAgent**, which handles all infrastructure tasks of your *Agent* without limiting what it can do.

````java
import io.logspace.agent.api.AbstractAgent;
import io.logspace.agent.api.order.TriggerType;

public class SimpleAgent extends AbstractAgent {

    public SimpleAgent() {
        super("simple-agent", "tutorial/simple-agent", TriggerType.Off, TriggerType.Event);
    }
}
````

This is already a valid *Agent* and fully configured with the values provided in its constructor (see [Architecture](/architecture) for what they mean), but it does not do anything yet.

If you want to create an *Agent* that reacts to a certain event in your application you need a method that you can call.<br/>
Since you are the only one calling this method, you can choose any signature you like:

````java
    public void execute() {
        DefaultEventBuilder eventBuilder = new DefaultEventBuilder(this.getId(), this.getSystem());
        Event event = eventBuilder.toEvent();
        this.sendEvent(event);
    }
````

>If you want to create an *Agent* that is triggered based on a **cron-expression** you have to overwrite the appropriate method. See the [Java Agent SDK](/java-agent-sdk) for specifics. The rest of this explanation does still apply.

You can use this **SimpleAgent** just like the **MemoryAgent** from [Getting Started](/getting-started/).<br/>
It will produce an *Event* which will be handled in the same way:

````java
public static void main(String[] args) {
  SimpleAgent simpleAgent = SimpleAgent.create();
  simpleAgent.execute();
}
````


On your console you will see something like this:

````
b2f05c25-ea6f-4cc2-ab8a-d5cd37e16bc0 (gid:none, pid:none) - simple-agent [null] - 2015-05-27 12:22:34.105: {}
````

While this is a valid *Event* it does not carry any specific information.<br/>
The next chapter will explain how we can change this.

##Building Events

logspace packages all information into *Events*. An *Event* is defined by the interface **io.logspace.agent.api.event.Event**. This interface defines the general attributes of all *Events*, like an ID and a timestamp.

Since some of those attributes are optional and others are mandatory, logspace relies on the [Builder Pattern] (http://en.wikipedia.org/wiki/Builder_pattern) to create *Events*. The best way to create your own *Events* is to create your own *EventBuilder* by extending the class **io.logspace.agent.api.event.AbstractEventBuilder**.

````java
public class SimpleEventBuilder extends AbstractEventBuilder {

    public SimpleEventBuilder(String agentId, String system) {
        super(agentId, system);
    }

    @Override
    protected Optional<String> getType() {
        return Optional.of("tutorial/simple-agent");
    }
}
````

The **Event-ID** and **Timestamp** are generated automatically by the AbstractEventBuilder, the **Agent-ID** and **System** are provided by your *Agent* and the **Event-Type** by your *EventBuilder*.<br/>

Using this *EventBuilder* will result in an *Event* similar to the one above.<br/>
It will carry the defined **Type** but it is still lacking any specific information.

In order to add specific information logspace uses *EventProperties*, which are defined by the interface **io.logspace.agent.api.event.EventProperty**.
There are specific *EventProperties* that can carry different types of data: **LongEventProperty**, **StringEventProperty**, **DateEventProperty**, etc.

The best way to add *EventProperties* to your *Events* is to add specific methods to your *EventBuilder* that will generate the appropriate *EventProperties*:

````java
    public void setExecutionCount(int executionCount) {
        this.addProperty("execution_count", executionCount);
    }
````
This method will create an *EventProperty* with the name **execution_count** and the type **int** and add it to your *Event*.

Using this *EventBuilder* in your *Agent* will produce different *Events*:

````java
public class SimpleAgent extends AbstractAgent {

    private int executionCount;

    public SimpleAgent() {
        super("simple-agent", "tutorial/simple-agent", TriggerType.Off, TriggerType.Event);
    }

    public void execute() {
        SimpleEventBuilder eventBuilder = new SimpleEventBuilder(this.getId(), this.getSystem());
        eventBuilder.setExecutionCount(++this.executionCount);
        Event event = eventBuilder.toEvent();
        this.sendEvent(event);
    }
}
````

````
2bca40c2-fc01-40b0-a70c-16a7d71cd40b (gid:none, pid:none) - simple-agent [tutorial/simple-agent] - 2015-05-27 12:40:10.660: {execution-count=1}
````

You can see that this *Event* has the type **tutorial/simple-agent** from your **SimpleEventBuilder** and that it carries the *EventProperty* **execution-count** with the value **1**.<br/>
Executing the *Agent* several times will produce *Events* with different property values:

````
ebc5b329-b2bb-44fc-8b5b-1affb27cc7dd (gid:none, pid:none) - simple-agent [tutorial/simple-agent] - 2015-05-27 12:42:04.142: {execution-count=1}
c4590bee-8943-4d23-982a-72e32cfd81cf (gid:none, pid:none) - simple-agent [tutorial/simple-agent] - 2015-05-27 12:42:04.144: {execution-count=2}
7ed72583-674a-4106-9aee-c3cbc33e9e4f (gid:none, pid:none) - simple-agent [tutorial/simple-agent] - 2015-05-27 12:42:04.145: {execution-count=3}
4c3c810d-f98e-4dd4-9db4-ff8f82bb5150 (gid:none, pid:none) - simple-agent [tutorial/simple-agent] - 2015-05-27 12:42:04.145: {execution-count=4}
````

Note how the **Event-ID** and the **Timestamp** are set automatically and each *Event* is properly initialized and routed to the console.<br/>


##The Do's and Don'ts
An *Event* can carry as many or as few *EventProperties* as desired.<br/>
There is no schema and if some of your *Events* are missing some *EventProperties*, logspace will not complain.

However there are still some things to consider:

* Only use letters, digits and underscores to construct the name of an *EventProperty*. This is a limitation of [Apache Solr](http://lucene.apache.org/solr/), which is currently used to store *Events* in the logspace *HQ* (see [Architecture](/architecture) for what the *HQ* does).
* Don't put the type of the *EventProperty* into its name. logspace keeps track of the type and the [Query API](/query-api) will have it available for you, so this will only lead to redundancy.
* Don't use large strings as value of an *EventProperty*. This is rarely useful and [Apache Solr](http://lucene.apache.org/solr/) will not accept any String longer than 32,768 characters.
* Avoid long running operations, blocking, waiting and multi-threading in your *Agents*. An *Agent* is always interrupting the normal execution of an application, so it should be as quick and less intrusive as possible.
