---
layout: default
title: Ready-to-use Agents
---
#Ready-to-use Agents

[CXF Agent](#cxf-agent)<br/>
[Journal Agent](#journal-agent)<br/>
[JVM Agent](#jvm-agent)<br/>
[Solr Agent](#solr-agent)<br/>
[OS Agents](#os-agents)<br/>
[Logspace Monitor](#logspace-monitor)<br/>

##CXF Agent
The CXF Agent is an *ApplicationAgent* for monitoring web request handling with [Apache CXF](https://cxf.apache.org/).

The *Agent* uses CXF's interceptor mechanism to create an *Event* for every handled request and collects the following *EventProperties*:

- protocol: The protocol of the endpoint
- domain: The host of the endpoint
- port: The port of the endpoint
- ip_address: The IP address of the caller
- path: The URI of the request
- query_string: The query of the request
- http_method: The HTTP method of the request
- request_id: The 'Request-Id' header of the request
- response_code: The response code
- location: The 'Location' header of the response
- duration: The time required for handling the request as measured by the agent


In order to measure the request time this *Agent* needs both an **InInterceptor** and an **OutInterceptor**.
In a [Spring](http://spring.io/) based application the configuration would look like this:

```xml
<bean class="org.apache.cxf.jaxrs.JAXRSServerFactoryBean">
  <property name="inInterceptors">
    <list>
      <bean id="LogspaceCxfInInterceptor" class="io.logspace.agent.cxf.CxfInAgent"/>
    </list>
  </property>
  <property name="outInterceptors">
    <list>
      <bean id="LogspaceCxfOutInterceptor" class="io.logspace.agent.cxf.CxfOutAgent">
        <property name="agentId" value="application-cxf" />
      </bean>  
    </list>
  </property>
</bean>
```

This *Agent* requires **logspace-agent-cxf** as additional dependency.

````xml
<dependency>
  <groupId>io.logspace</groupId>
  <artifactId>logspace-agent-cxf</artifactId>
  <version>{{site.data.logspace.version}}</version>
  <scope>runtime</scope>
</dependency>
````


##Journal Agent
This is an *ApplicationAgent* that can be used for tagging a specific point in time with a category and a message.

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

This *Agent* requires **logspace-agent-journal** as additional dependency.

````xml
<dependency>
  <groupId>io.logspace</groupId>
  <artifactId>logspace-agent-journal</artifactId>
  <version>{{site.data.logspace.version}}</version>
</dependency>
````

##JVM Agent
The JVM Agent is a special *SchedulerAgent* that collects information about the JVM in which it is running.

It creates the following *EventProperties*:

- CPU
 - process_cpu_load: The CPU load caused by the JVM
 - process_cpu_time: The total amount of CPU time consumed by the JVM

- File Descriptors
 - max_file_descriptor_count: The maximum number of file descriptors of the JVM
 - open_file_descriptor_count: The number of open file descriptors of the JVM

- Garbage Collection 
 - garbagecollector_run_count: The total number of collections the garbage collect did
 - garbagecollector_time: The total amount of time spent by the garbage collector

- Threads
 - thread_count: The number of live threads inside the JVM; this includes both daemon and non-daemon threads
 - daemon_thread_count: The number of live daemon threads inside the JVM

- Memory
 - initial_heap_memory: The initial heap memory of the JVM
 - max_heap_memory: The allowed maximum heap memory of the JVM
 - used_heap_memory: The actually used heap memory of the JVM
 - committed_heap_memory: The amount of heap memory committed for the JVM
 - initial_non_heap_memory: The initial non-heap memory of the JVM
 - max_non_heap_memory: The maximum allowed non-heap memory of the JVM
 - used_non_heap_memory: The actually used non-heap memory of the JVM
 - committed_non_heap_memory: The amount of non-heap memory committed for the JVM

- Class Loading
 - loaded_class_count: The number of classes currently loaded in the JVM
 - total_loaded_class_count: The total number of classes that have been loaded since the start of the JVM
 - unloaded_class_count: The The total number of classes that have been unloaded since the start of the JVM

The *Agent* will also create an *Event* when the JVM is started, stopped or if the agent is attached to a running JVM.

To use this *Agent* the JVM must be started with the following additional command line parameters:

```bash
java -Dio.logspace.jvm-identifier=MyApplicationJVM\
     -Dio.logspace.jvm-logspace.config=logspace.json\
     -javaagent:logspace-jvm-agent.jar\
     ...
```

The parameter **io.logspace.jvm-identifier** defines an identifier for this *JVM Agent* that will help you identify the *Events* produced by this *Agent*. This should be a unique value.<br/>
The parameter **io.logspace.jvm-logspace.config** defines the configuration to be used for the *AgentController*. This value should be a URL or a file path.

>The *JVM Agent* uses its own *AgentController* which is not shared by any other *Agent* that might also be running inside the same JVM.

>The *JVM Agent* contains all required libraries in a [Shaded JAR](https://maven.apache.org/plugins/maven-shade-plugin/), so you don't need any additional libraries in your application's classpath.
This means that the class-names for the *AgentControllers* are shaded as well and their packages must be adjusted from **io.logspace.agent** to **io.logspace.jvm.agent**.<br/>
E.g. the class-name for the ```HqAgentController``` would be:

>```json
{
  "class-name" : "io.logspace.jvm.agent.hq.HqAgentController",
}
```
>
>All other configuration parameters are unaffected.


##Solr Agent
The Solr Agent combines an *ApplicationAgent* and a *SchedulerAgent* for monitoring individual cores in [Apache Solr](http://lucene.apache.org/solr/).

The *Agent* uses Solr's event listener mechanism to collect information about commits and can also collect statistical information about the core.
It collects the following *EventProperties*:

- New-Searcher
 - core_name: The name of the core
 - warmup_time: The warmup time for the searcher

- Post-Commit
 - core_name: The name of the core

- Post-Soft-Commit
 - core_name: The name of the core

- Statistics
 - core_name: The name of the core
 - request_count: The total number of requests handled
 - average_request_time: The average request time for all handled requests
 - average_requests_per_second: The average number of requests per second 
 - 75th_percentile_request_time: The 75th percentile of the request time
 - 95th_percentile_request_time: The 95th percentile of the request time
 - 99th_percentile_request_time: The 99th percentile of the request time
 - 999th_percentile_request_time: The 999th percentile of the request time
 - document_count: The number of live documents in the core
 - updates: The total number of updates executed
 - deletes: The total number of deletes executed
 - commits: The total number of commits executed
 - index_size: Size of the index on disk in bytes
 - generation: Generation of the core
 - index_version: Index version of the core
 - is_master: Whether or not the core is currently the master
 - is_slave: Whether or not the core is currently a slave
 - field_cache_size: The current size of the field cache
 - field_cache_hit_ratio: The cumulative hit ratio of the field cache
 - field_value_cache_size: The current size of the field value cache
 - field_value_cache_hit_ratio: The cumulative hit ratio of the field value cache
 - query_cache_size: The current size of the query cache
 - query_cache_hit_ratio: The cumulative hit ratio of the query cache
 - document_cache_size: The current size of the document cache
 - document_cache_hit_ratio: The cumulative hit ratio of the document cache
 - filter_cache_size: The current size of the filter cache
 - filter_cache_hit_ratio: The cumulative hit ratio of the filter cache

To install this *Agent* add the ```SolrAgentInstaller``` as a listener to your solrconfig.xml:

```xml
<!-- Register Logspace Solr Agent -->
<listener event="firstSearcher" class="io.logspace.agent.solr.SolrAgentInstaller" />
```

This *Agent* requires the **logspace-agent-controller.jar** and the **logspace-agent-solr.jar** in the `sharedLib` directory of your Solr installation.


##OS Agents
Logspace offers four different *SchedulerAgents* for collecting information about the CPU, disk, memory and swap utilization as reported by your operating system.
> Not all operating systems support retrieving all values


The ```CpuAgent``` collects the following **EventProperties**:

- processor_count: The number of available processors
- system_load_average: The average system load for the last minute
- system_cpu_load: The recent CPU usage

The ```DiskAgent``` collects the following **EventProperties**:

- disk_path: The root path of the disk
- disk_total_space: The total number of bytes on the disk
- disk_usable_space: The number of bytes on the disk that are available to the JVM
- disk_unallocated_space: The number of bytes on the disk that are not allocated
- disk_used_space: The number of bytes on the disk that are in use (total - unallocated)

>For every invocation the ```DiskAgent``` produces one *Event* per root directory.

The ```MemoryAgent``` collects the following **EventProperties**:

- total_memory: The total amount of physical memory in bytes
- free_memory: The amount of free physical memory in bytes
- used_memory: The amount of used physical memory in bytes (total - free)
- committed_virtual_memory: The amount of virtual memory committed to the JVM

The ```SwapAgent``` collects the following **EventProperties**:

- swap_total_space: The total amount of swap space in bytes
- swap_free_space: The amount of free swap space in bytes
- swap_used_space: The amount of used swap space in bytes (total - free) 

To use one of these *Agents*, they only need to be instantiated with their factory method:

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

> Currently only the ```HqAgentController``` contains the scheduler required to operate *SchedulerAgents*.


These *Agents* require **logspace-agent-os** as additional dependency.

````xml
<dependency>
  <groupId>io.logspace</groupId>
  <artifactId>logspace-agent-os</artifactId>
  <version>{{site.data.logspace.version}}</version>
</dependency>
````

##Logspace Monitor
The *Logspace Monitor* packages all OS Agents into an executable JAR, which can easily be used to periodically collect information about a system.

Simply configure the path to the logspace.json and start the monitor:

```bash
java -Dlogspace.config=logspace.json\
     -jar logspace-monitor-runnable.jar
```
