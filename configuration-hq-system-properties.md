---
layout: default
title: Configuration HQ - System Properties
---

#System Properties

[Logging](#logging) <br/>
[Port](#port) <br/>
[Solr](#solr) <br/>
[Data Directory](#data-directory) <br/>


## Logging
The log files will be created in the directory denoted by the system property `log-path`, e.g.

```
-Dlog-path=/logspace/logs
```

This setting is required.
>To prevent file system pollution from accidental misconfiguration, the directory must already exist and will not be created.

## Port
The port of the *Logspace HQ* can be configured with the system property `port`, e.g.

```
-Dport=4569
```

This settings is optional and has the **default value 4567**.

## Solr
The URL to the Apache Solr installation can be configured with the system property `logspace.solr.base-url`, e.g.

```
-Dlogspace.solr.base-url=http://localhost:8983/solr/logspace.
```

This setting is required.

Logspace requires at least Solr 5.3.1<br/>
The Solr configuration files are part of the *Logspace HQ* [distribution](/download) and can also be found [here](https://github.com/Indoqa/logspace/tree/logspace-{{site.data.logspace.version}}/logspace-hq-solr-plugin/src/main/resources/META-INF/solr/logspace/conf/).
More details on how to prepare a suitable Solr instance can be found [here](/setting-up-solr).

## Data Directory
The *Logspace HQ* uses a single directory to store all data required during its operation.<br/>It contains the *capabilities* and *[orders](/configuration-hq-orders)* of *AgentControllers* and the configuration of the *[Spaces](/configuration-hq-spaces)*, each in its own sub-directory:

- data
 - capabilities
 - orders
 - spaces


The data directory is configured with the system property `logspace-hq-webapp.data-directory`, e.g.

```
-Dlogspace.hq-webapp.data-directory=/logspace/data
```

This settings is required.
>To prevent file system pollution from accidental misconfiguration, the data directory and its sub-directories must already exist and will not be created.
