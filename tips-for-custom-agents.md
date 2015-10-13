---
layout: default
title: Tips for custom Agents
---

# Tips for custom Agents

An *Event* can carry as many or as few *EventProperties* as desired.<br/>
There is no schema and if some of your *Events* are missing some *EventProperties*, Logspace will not complain.

However there are still some things to consider:

* Only use letters, digits and underscores to construct the name of an *EventProperty*. This is a limitation of [Apache Solr](http://lucene.apache.org/solr/), which is currently used to store *Events* in the *Logspace HQ* (see [System Overview](/system-overview) for what the *Logspace HQ* does).
* Don't put the type of the *EventProperty* into its name. Logspace keeps track of the type and the [Query API](/query-api) will have it available for you, so this will only lead to redundancy.
* Don't use large strings as value of an *EventProperty*. This is rarely useful and [Apache Solr](http://lucene.apache.org/solr/) will not accept any String longer than 32,768 characters.
* Avoid long running operations, blocking, waiting and multi-threading in your *Agents*. An *Agent* is always interrupting the normal execution of an application, so it should be as quick and less intrusive as possible.
