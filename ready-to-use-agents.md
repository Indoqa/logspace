---
layout: default
title: Ready-to-use Agents
---
#Ready-to-use Agents
The following agents can be used out of the box, they can either be integrated into your application or can be started in your environment to collect various information.

To send the produced events to the Logspace HQ [this logspace.json](/configuration-hq-agent-controller) can be used.


##Logspace CXF Agent
The CXF agent records various properties of a request handled by CXF, e.g. duration, path, query string, response code, etc.

To use this agent in a [Spring](http://spring.io/) based application the following beans must be defined:

```xml
<bean id="LogspaceCxfInInterceptor" class="io.logspace.agent.cxf.CxfInAgent"/>
<bean id="LogspaceCxfOutInterceptor" class="io.logspace.agent.cxf.CxfOutAgent">
  <property name="agentId" value="application-cxf" />
</bean>  
```

Also these interceptors need to be configured in your services. For example in the JAXRSServerFactoryBean:

```xml
<bean class="org.apache.cxf.jaxrs.JAXRSServerFactoryBean">
  <property name="inInterceptors">
    <list>
      <ref bean="LogspaceCxfInInterceptor" />
    </list>
  </property>
  <property name="outInterceptors">
    <list>
      <ref bean="LogspaceCxfOutInterceptor" />
    </list>
  </property>
</bean>
```


##Logspace Journal Agent
This is a simple agent which can be used for tagging a specific point in time with a category and a message.

```java
package io.logspace.demo;

import io.logspace.agent.journal.JournalAgent;

public class MyApplication {

    public static void main(String[] args) {
        JournalAgent journalAgent = JournalAgent.create("journal");
        journalAgent.triggerEvent("info", "Begin work");

        work();

        journalAgent.triggerEvent("info", "Finished work.");
    }

    private static void work() {
        //do your work here
    }
}
```


##Logspace JVM Agent
The JVM agent will collect information like version and vendor of the JVM, memory usage, garbage collection, etc.
It will produce events in the case a JVM is started, stopped or if the agent is attached to a running JVM.

To hook up the agent to your jvm it must be started with the following parameters:

```bash
java -Dio.logspace.jvm-identifier=MyApplicationJVM\
     -Dio.logspace.jvm-agent-description-url=logspace.json\
     -javaagent:logspace-jvm-agent.jar\
     ...
```

The logspace.json configuration file for the JVM agent is similar to [this logspace.json](/configuration-hq-agent-controller).
To achieve the best possible compatibility this agent includes all necessary classes, and these are repackaged to avoid class loading issues.
Therefore the class-name for the HqAgentController for the JVM agent is:

```json
{
  ...
  "class-name" : "io.logspace.jvm.agent.hq.HqAgentController",
  ...
}
```


##Logspace Monitor
The Logspace monitor packages all OS agents into a runnable jar, which can be installed as a service on a machine to periodically collect information about the CPU, disk, memory and swap utilization of the system.

Simply configure the path to the logspace.json and start the monitor:

```bash
java -Dlogspace.config=logspace.json\
     -jar logspace-monitor-runnable.jar
```


##Logspace OS Agents
The OS agents consist of different agents for collecting information about the CPU, disk, memory, swap usage of a system.

To use all of them in your application put the logspace.json on your applications classpath and create the agents accordingly:

```java
package io.logspace.demo;

import io.logspace.agent.os.CpuAgent;
import io.logspace.agent.os.DiskAgent;
import io.logspace.agent.os.MemoryAgent;
import io.logspace.agent.os.SwapAgent;

public class MyApplication {

    public static void main(String[] args) {
        CpuAgent.create();
        DiskAgent.create();
        MemoryAgent.create();
        SwapAgent.create();

        work();
    }

    private static void work() {
        //do your work here
    }
}
```


##Logspace Solr Agent
The [Solr](http://lucene.apache.org/solr/) agent can be used to record statistical information about a [Solr Core](https://wiki.apache.org/solr/SolrTerminology) like request times, caches, updates, replication status and application triggered information about commits and searchers.

To install the Solr agent in your Solr installation the following steps need to be done:

* Add the following lines to your solrconfig.xml for each Core:

```java
<!-- Register logspace Solr Agents -->
<listener event="firstSearcher" class="io.logspace.agent.solr.SolrAgentInstaller" />
```

* The logspace-agent-controller.jar and the logspace-agent-solr.jar must be present in the `sharedLib` directory.

* Configure the logspace.json via the following system property at startup of your Solr installation:

```bash
-Dlogspace.config=/logspace.json
```