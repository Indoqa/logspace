---
layout: default
title: Configuration HQ AgentController
---

#Configuration HQ AgentController

##Parameters

- `base-url`: The url to the HQ, e.g. http://localhost:4567.
- `space-token`: The space-token used for authentication, e.g. development.
- `queue-file`: The path to the file which will be used for queueing events for upload.

##Configuration via logspace.json

To use the HqAgentController in your Agent, simply put the `logspace.json` on your classpath.
The configuration will be loaded and the **io.logspace.agent.hq.HqAgentController** will be instantiated with the provided parameters.

Example logspace.json

```json
{
  "id" : "AgentDescriptionId",
  "class-name" : "io.logspace.agent.hq.HqAgentController",
  "parameters" : {
    "space-token" : "development",
    "base-url" : "http://localhost:4567",
    "queue-file" : "./logspace-event-queue.dat"
  }
}
```