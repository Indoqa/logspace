---
layout: default
title: Configuration HQ
---

#Configuration HQ

##System properties

The log files will be created in the directory denoted by the system property `log-path`, e.g.

```
-Dlog-path=/logspace/logs
```

The Logspace port can be configured via the systemproperty `port`, e.g. 

```
-Dport=4569
```

To configure the url to solr set the systemproperty `logspace.solr.base-url=`, e.g.

```
-Dlogspace.solr.base-url=http://localhost:8983/solr/logspace.
```

##Spaces

[Spaces](/multiple-spaces) have to be defined as files in the directory configured with the property `logspace.hq-webapp.space-tokens-directory`.

The filename is **space** with the extension **space-tokens**.

Example content of demo.space-tokens

```
demo1234
```

Where `demo` is the space and `demo1234` is an authentication token for this space.

##Configurations

The configuration files for the agent-orders have to be stored in the directory defined by the `logspace.hq-webapp.configs-directory` property.

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

##Capabilities

The property `logspace.hq-webapp.capabilities-directory` defines the directory in which the agents capabilities will be stored.
