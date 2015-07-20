---
layout: default
title: Demo
---


# Logspace Demo
The *Logspace Demo* is a prepared setup that allows you to test-drive Logspace on your own computer in just one minute.

>All files will be stored in your temp directory.<br/>
See "Behind the Curtain" for details.

## Prerequsites
The only requirement for running the *Logspace Demo* is that you have installed a Java Runtime Environment 8 (JRE8).
We encourage you to use the latest version and stay up-to-date with security and bug fix releases.

## Step 1: Download the Logspace Distribution
First you need to download the **logspace-dist.zip** and extract it.

## Step 2: Start the Logspace HQ
The next step is to open a terminal, change to the directory you extracted the **logspace-hq-webapp-runnable.jar** to and execute the following command:

````
java -jar logspace-hq-webapp-runnable.jar --demo
````

This will start the *Logspace HQ* in demo mode using port 4567.<br/>
You can browse to [http://localhost:4567](http://localhost:4567) to use the frontend.

Unless you have already collected some *Events* with this mode before, you will not find any data.
To change this, we will start the *Logspace Monitor* in demo mode as well.


## Step 3: Run the Logspace Monitor
Now open a second terminal, change to the directory you extracted the **logspace-monitor-runnable.jar** to and execute the following command:

````
java -jar logspace-monitor-runnable.jar --demo
````

This will start the *Logspace Monitor* in demo mode.
It will automatically try to connect to a *Logspace HQ* on the same computer on port 4567 and upload its monitoring data.


## Step 4: Inspect your *Events*
With the *Monitor* and *HQ* running, you are collecting *Events* about the computer's CPU, memory, disk and swap utilization. Those *Events* are uploaded to the HQ and stored for further analysis.

>Uploading, preparing and storing *Events* takes a little time, since it happens in a background thread using batch processing.<br/>
In demo mode you should see *Events* after about 20 seconds.<br/>
In production mode you can configure the upload process, which has an effect on both upload delay and network overhead.

The interactive frontend works with *TimeSeries*.<br/>
A *TimeSeries* defines a single *EventProperty* from a specific *Agent* and an *Aggregation* for the values of that *EventProperty* for a certain window of time. It also contains settings for how to render it in a chart.

1. Begin by selecting the time window and gap: This applies to all *TimeSeries*
2. Define a *TimeSeries*:
	1. Select an *Agent*
	2. Select the *EventProperty*
	3. Select an *Aggregation*
	4. Select a color
	5. Select a scale or simply use the default scaling

You can always edit a *TimeSeries* and select a different *EventProperty* from the same *Agent* or change the *Aggregation*, color or scale.

*TimeSeries* can also be exported and re-imported again, allowing you to restore frequently required charts quickly or share it with someone else.

## Behind the Curtain

The *Logspace Demo* uses the normal JARs, which can also be used in production and applies a special configuration.

It prepares the directory **logspace-demo** in your temp directory and creates directories for configuration, logging, and collected data inside it. Part of this configuration are *AgentOrders* for a *Logspace Monitor* running in demo mode.

The *EventStore* is configured as an embedded Solr instance which runs in the same JVM as the *Logspace HQ*. This Solr instance is not accessible by HTTP, nor does it offer any of the administrative or configuration services usually available.

All data and logs collected with the demo mode are persistent!<br/>
You can stop and restart the demo and continue to work with your collected data as long as your temp directory is not cleared.<br/>
You can also collect *Events* from other *Agents*, provided you configure them appropriately.

You are free to inspect the configuration and modify it, but keep in mind that Logspace will rewrite the configuration files with every start.<br/>

If you want to get rid of the collected data just stop Logspace and delete the directory **logspace-demo**.
