---
layout: default
title: Controller Configuration
---

# Controller Configuration

[HqAgentController](#hqagentcontroller) <br/>
[ConsoleAgentController](#consoleagentcontroller) <br/>
[LoggingAgentController](#loggingagentcontroller) <br/>
[DummyAgentController](#dummyagentcontroller) <br/>

Logspace uses a JSON-based configuration file to determine which *AgentController* to use.<br/>
The configuration is located by inspecting the following locations:

1. the file **logspace.json** in the current directory.
2. the file **logspace-test.json** in your application's classpath
3. the file **logspace.json** in your application's classpath
4. the file **logspace-default.json** in your application's classpath

You can also use the system-property **logspace.config** to overwrite this mechanism and provide a URL or a relative/absolute file path to a configuration file.

The content of the configuration file should look like this:

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
    "base-url" : "http://localhost:4567",
    "queue-directory" : "${java.io.tmpdir}/logspace"
  }
}
````

- The parameter **space-token** is used for authentication so that only authorized *AgentControllers* can send *Events* to the *Logspace HQ*.
- The parameter **base-url** is used to establish a HTTP connection to the headquarters.
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

## DummyAgentController
The ```DummyAgentController``` quietly discards all *Events*.

````json
{
  "id" : "controller-id",
  "class-name" : "io.logspace.agent.impl.DummyAgentController"
}
````