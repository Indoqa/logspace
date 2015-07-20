---
layout: default
title: Configuration HQ - Orders
---

#Orders

The configuration files for the agent orders are stored in the directory defined by the `logspace.hq-webapp.orders-directory` property.

The filename is used as the id of the agentController. These settings need to be configured:

  - Agent orders - for each agent
    - The `id` of the agent.
    - The `trigger-type` can be `OFF`, `CRON` or `EVENT`.
    - A `trigger-parameter` if needed for example a cron expression for the CRON trigger.
  - `commit-max-count`: if this amount of events is reached an upload will be triggered.
  - `commit-max-seconds`: if this amount of seconds is reached since the last upload an upload will be triggered.

Example content of logspace-demo.json

```json
{
  "agent-orders" : [
    {
      "id" : "os/cpu",
      "trigger-type" : "CRON",
      "trigger-parameter" : "0/10 * * * * ? *"
    },
    {
      "id" : "os/memory",
      "trigger-type" : "CRON",
      "trigger-parameter" : "0/10 * * * * ? *"
    }
  ],
  "commit-max-count" : 1000,
  "commit-max-seconds" : 10
}
```

