---
layout: default
title: Configuration HQ - System Properties
---

#System Properties

The log files will be created in the directory denoted by the system property `log-path`, e.g.

```
-Dlog-path=/logspace/logs
```

The Logspace port can be configured via the system property `port`, e.g.

```
-Dport=4569
```

To configure the url to Apache Solr set the system property `logspace.solr.base-url=`, e.g.

```
-Dlogspace.solr.base-url=http://localhost:8983/solr/logspace.
```

The [orders](/configuration-hq-orders) for the agents have to be stored in the directory defined by the `logspace.hq-webapp.orders-directory` property.

```
-Dlogspace.hq-webapp.orders-directory=/logspace/orders
```

The property `logspace.hq-webapp.capabilities-directory` defines the directory in which the agents capabilities will be stored.

```
-Dlogspace.hq-webapp.capabilities-directory=/logspace/capabilities
```

The property `logspace.hq-webapp.space-tokens-directory` defines the directory in which the spaces and their tokens are configured:

```
-Dlogspace.hq-webapp.space-tokens-directory=/logspace/space-tokens
```
