---
layout: default
title: Configuration HQ - Orders
---

#Orders

Orders control, when *SchedulerAgents* are to be executed and if *ApplicationAgents* should produce *Events*.<br/>

The ``HqAgentController`` retrieves its *AgentControllerOrder* from the *Logspace HQ* and applies it to all its *Agents*. It will also poll for updates to its order and apply the changes as soon as they are retrieved.

Each *AgentControllerOrder* is stored as a single file in JSON format in the ``orders`` sub-directory inside the [data directory](/configuration-hq-system-properties#data-directory). The order belongs to the ``HqAgentController`` that carries the same ID as the order. This ID is also used as the name of the file, with the extension ".json".<br/>
So the order for the ``HqAgentController`` with the ID `logspace-controller-01` is to be stored in the file `<data-directory>/orders/logspace-controller-01.json`.

The *AgentControllerOrder* contains individual *AgentOrders* for the *Agents* of the controller and the commit setting for the controller itself.
Each *AgentOrder* contains the ID of the *Agent* it belongs to and its trigger settings.

Here's an example:

```json
{
  "agent-orders" : [
    {
      "id" : "my-scheduler-agent",
      "trigger-type" : "SCHEDULER",
      "trigger-parameter" : "0/10 * * * * ? *"
    },
    {
      "id" : "my-application-agent",
      "trigger-type" : "APPLICATION"
    },
    {
      "id" : "my-disabled-agent",
      "trigger-type" : "OFF"
    }    
  ],
  "commit-max-seconds" : 60
}
```

The *Agent* with the ID `my-scheduler-agent` is to be executed by the *Scheduler* of the ``HqAgentController``. The *Scheduler* will use the Cron-Expression from the `trigger-parameter` to determine when to execute this *Agent*. 

The *Agent* with the ID `my-application-agent` is to executed by the application. This means the *Agent* has to be called directly from the application's code. No trigger parameter is required for this type of trigger.

The *Agent* with the ID `my-disabled-agent` is not to be executed.<br/>
If this *Agent* is an *ApplicationAgent* it will still be called from the application's code, but the *Agent* will be able to determine that it has been disabled and should not produce an *Event*.

>All *Agents* that do not have an *AgentOrder* will be treated like having the trigger type `OFF`.

The parameter `commit-max-seconds` determines, how much time may pass before a collected *Event* is being sent to the *Logspace HQ*. The *Controller* may commit more often, if a lot of *Events* are produced or a commit is forced programmatically.<br/>
Having longer commit delays can reduce the number of network accesses, because it allows to collect more *Events* before sending them in a single upload package.