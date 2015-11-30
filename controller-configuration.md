---
layout: default
title: Controller Configuration
---

# Controller Configuration

[HqAgentController](#hqagentcontroller) <br/>
[ConsoleAgentController](#consoleagentcontroller) <br/>
[LoggingAgentController](#loggingagentcontroller) <br/>
[DummyAgentController](#dummyagentcontroller) <br/>

Logspace uses a lookup mechanism to automatically find a configuration for the *AgentController*. 
By default a JSON-based configuration is expected in one of the following locations:

1. the file **logspace.json** in the current directory.
2. the file **logspace-test.json** in your application's classpath.
3. the file **logspace.json** in your application's classpath.
4. the file **logspace-default.json** in your application's classpath - this file is part of the **logspace-agent-controller** module.

You can also use the system-property **logspace.config** to overwrite this mechanism and provide a URL or a relative/absolute file path to a JSON configuration file.

The content of this configuration file should look like this:

````json
{
  "id" : "controller-id",
  "class-name" : "controller-class-name",
  "parameters" : {
    "key" : "value"
  }
}
````

- The parameter **id** defines the identifier of your *AgentController*. It should be a unique value since it is used to distinguish between different controllers.
- The parameter **class-name** defines which implementation of *AgentController* to load.
- The parameter **parameters** contains key-value-pairs. Which parameters you need depends on the controller class.

## HqAgentController
The ```HqAgentController``` uploads all *Events* to the *Logspace HQ*, and allows execution of *SchedulerAgents*.

````json
{
  "id" : "controller-id",
  "class-name" : "io.logspace.agent.hq.HqAgentController",
  "parameters" : {
    "space-token" : "8kn3GGXvVVyeeE2m2eyu",
    "hq-url" : "http://localhost:4567",
    "queue-directory" : "${java.io.tmpdir}/logspace"
  }
}
````

- The parameter **space-token** is used for authentication so that only authorized *AgentControllers* can send *Events* to the *Logspace HQ*. (see [Spaces](/configuration-hq-spaces) for how to configure Space-Tokens)
- The parameter **hq-url** defines the URL of the *Logspace HQ* to communicate with.
- The parameter **queue-directory** is a file system path. It is used to buffer events and to enable retries in case the *Logspace HQ* cannot be reached. You can reference system-properties by writing "${property-name}".

## ConsoleAgentController
The ```ConsoleAgentController``` writes all *Events* directly to the console (System.out).

````json
{
  "id" : "controller-id",
  "class-name" : "io.logspace.agent.console.ConsoleAgentController",
  "parameters" : {
    "message-pattern" : "{id} (gid:{global-id}, pid:{parent-id}) - {agent-id} [{type}] - {timestamp}: {properties}"
  }
}
````

- The parameter **message-pattern** determines how *Events* are written to the console. The available placeholders are **{id}**, **{global-id}**, **{parent-id}**, **{agent-id}**, **{type}**, **{timestamp}**, **{properties}**.

## LoggingAgentController
The ```LoggingAgentController``` writes all *Events* to an SLF4J logger.

````json
{
  "id" : "controller-id",
  "class-name" : "io.logspace.agent.logging.LoggingAgentController",
  "parameters" : {
    "message-pattern" : "{id} (gid:{global-id}, pid:{parent-id}) - {agent-id} [{type}] - {timestamp}: {properties}"
  }
}
````

- The parameter **message-pattern** determines how *Events* are written to the console. The available placeholders are **{id}**, **{global-id}**, **{parent-id}**, **{agent-id}**, **{type}**, **{timestamp}**, **{properties}**.

## TestAgentController
The ```TestAgentController``` collects all *Events* and allows to inspect them afterwards.

As the name suggests, this controller is intended only for test environments as it will collect and keep all *Events* in memory.<br/>
In a production environment this will most certainly fill the heap of your JVM!

````json
{
  "id" : "controller-id",
  "class-name" : "io.logspace.agent.test.TestAgentController",
  "parameters" : {
    "output-file" : "./target/logspace-test-events.json"
  }
}
````

- The parameter **output-file** is a path to a file that will be used to store all collected events to. You can reference system-properties by writing "${property-name}".<br/>The purpose of the file is purely for finding problems after a test failed and the *Events* need to be inspected for analysis.<br/>To prevent accidental polution of the file system, the ```TestAgentController``` will not create any directories.

During your unit-tests you can access collected *Events* programmatically:

````java
@Test
public void test() {
    TestAgentController.installIfRequired("./target/test-events.json");

    assertEquals("Expected no Events at the beginning.", 0, TestAgentController.getCollectedEventCount());

    JournalAgent.create("test-journal").triggerEvent("test-category", "test-message");
    assertEquals("Expected one Journal Event.", 1, TestAgentController.getCollectedEventCount());

    Event event = TestAgentController.getCollectedEvent(0);
    assertEquals("test-category", TestAgentController.getProperty(event.getStringProperties(), "category"));
    assertEquals("test-message", TestAgentController.getProperty(event.getStringProperties(), "message"));

    AgentControllerProvider.shutdown();
}
````

## DummyAgentController
The ```DummyAgentController``` quietly discards all *Events*.

````json
{
  "id" : "controller-id",
  "class-name" : "io.logspace.agent.impl.DummyAgentController"
}
````
&nbsp;

# Using a Different Type of Configuration

You can also load a configuration from a different format by creating your own implementation of ```io.logspace.agent.api.AgentControllerDescriptionDeserializer```, adding it to the classpath and configuring the system-property ```logspace.configuration-deserializer```.
In this case you will also have to provide the location of your configuration with the system-property ```logspace.config```.

````
java -Dlogspace.config=logspace-config.xml -Dlogspace.config-deserializer=com.mypackage.logspace.ConfigXmlDeserializer -jar my-application.jar 
````