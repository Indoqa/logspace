---
layout: default
title: Separate your Events - Spaces
---

# Separating Events - Markers &amp; Spaces

## Markers

The *Marker* is one of the basic attributes of the *Event*.<br/>
It is an optional value that can be used to group *Events* that cannot be connected to each other by any other means.

The *Marker* is part of the *AgentController* configuration and will be added to every *Event* that is handled by it.

```json
{
  "id" : "agent-controller-id",
  "class-name" : "io.logspace.agent.hq.HqAgentController",
  "parameters" : {
    "space-token" : "development",
    "base-url" : "http://localhost:4567",
    "queue-directory" : "${java.io.tmpdir}",
    "marker" : "application-test-001"
  }
}
```

It is still possible to compare and analyze *Events* with different Markers.


##Spaces

*Spaces* are used to distinguish *Events* from different sources inside the *Logspace HQ*.<br/>
They could represent different servers, different projects or different countries.

When an *Event* is uploaded to the *Logspace HQ* a *Space-Token* is required for authentication. If a valid token is provided, the *Event* will be stored in the *Space* that token belongs to.

Every *Space* can have as many *Space-Tokens* as required, allowing you to use them to control write-permissions to your *Spaces*. 

It is still possible to compare and analyze *Events* stored in different *Spaces*.