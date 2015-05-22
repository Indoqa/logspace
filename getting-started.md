---
layout: default
title: Getting Started
---

# Getting Started
Using logspace is as easy as using a logging framework:
Just add the logspace-agent-all.jar to your application's classpath and you are ready to go.


## Usage

In logspace you use *Agents* to collect relevant data and package it into *Events*.<br/>
Doing this is a simple method call:

````java
public static void main(String[] args) {
  MemoryAgent memoryAgent = MemoryAgent.create();
  memoryAgent.execute();
}
````

You just measured your computer's memory utilization and packaged it into an *Event*.

On your console you saw something like this:

>30f19237-4134-46af-ae6d-277837daed4d (gid:none, pid:none) - os/memory [os/memory] - 2015-05-22 13:29:03.100: {total_memory=16754085888, free_memory=11813036032, used_memory=4941049856, committed_virtual_memory=462962688}

What is that output?<br/>
In the background logspace created an *AgentController* and the *MemoryAgent* forwarded the *Event* with the memory utilization data to it. logspace comes with different implementations of *AgentController*, each handling *Events* in a different way.<br/>
In our example above, logspace used the *ConsoleAgentController* which writes all *Events* to the console.

The chapter "Configuration" explains how you can use other *AgentControllers*.

## Configuration

If you just want to write your *Events* to console you don't need any configuration at all.
logspace will do this by default to make sure none of your *Events* are lost. It is convenient during development but sometimes you might want something else.

You can provide a configuration to define which *AgentController* is used:<br/>
Create a file called "logspace.json" and put it in your working directory or the application's classpath.<br/>
The file contains a simple configuration object written in JSON. The default configuration looks like this:

````json
{
  "id" : "logspace-default",
  "class-name" : "io.logspace.agent.console.ConsoleAgentController"
}
````

If you are using slf4j you could use the *LoggingAgentController* to write all *Events* to you application's logfile.
The configuration looks like this:

````json
{
  "id" : "logspace-logging",
  "class-name" : "io.logspace.agent.logging.LoggingAgentController"
}
````

Whatever *AgentController* you chose, your application's code does not need to change.<br/>
You simply invoke *Agents* and logspace takes care of handling the *Events* for you.