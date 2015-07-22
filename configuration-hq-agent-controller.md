---
layout: default
title: Configuration HqAgentController
---

#Configuration HqAgentController

##Parameters

- `base-url`: The url to the HQ, e.g. http://localhost:4567.
- `space-token`: The space-token used for authentication, e.g. development.
- `queue-directory`: The path to the directory where the files, for queueing events for upload, will be stored.

##Configuration via logspace.json

To use the HqAgentController in your Agent, simply put the `logspace.json` on your classpath.
The configuration will be loaded and the **io.logspace.agent.hq.HqAgentController** will be instantiated with the provided parameters.

Example logspace.json

```json
{
  "id" : "AgentControllerId",
  "class-name" : "io.logspace.agent.hq.HqAgentController",
  "parameters" : {
    "space-token" : "development",
    "base-url" : "http://localhost:4567",
    "queue-directory" : "./queues/"
  }
}
```
