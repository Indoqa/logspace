---
layout: default
title: Frontend
---

# Frontend

[Adding a new Time-Series](#adding-a-new-time-series)<br/>
[Selecting a Scale](#selecting-a-scale)<br/>
[Selecting the Time Window](#selecting-the-time-window)<br/>
[Chart Controls](#chart-controls)<br/>
[Export and Import](#export-and-import)

## Adding a new Time-Series

1. Start by clicking the ``Add`` button. [![Frontend Select Agent](/assets/images/frontend-add-time-series.png)](/assets/images/frontend-add-time-series.png)
2. Filter the visible *Agents* by selecting one of the *Spaces*, *Systems* or *Properties*, or by entering text and select the one providing the *Property* you want to analyze.
3. Select the *Property*, *Aggregation* and *Color*. [![Fontend Select Property](/assets/images/frontend-select-property.png)](/assets/images/frontend-select-property.png)
4. Save your *Time-Series* to add it to the chart [![Frontend Chart](/assets/images/frontend-chart.png)](/assets/images/frontend-chart.png)

## Selecting a Scale

You can choose between 3 different kinds of scales for your *Time-Series*:

[![Frontend Scale](/assets/images/frontend-select-scale.png)](/assets/images/frontend-select-scale.png)

1. Existing Scale:<br/>
Uses the same scale as another *Time-Series*, scaling accordingly. 
2. Data:<br/>
Scale the Y-axis so that all values fit inside the visible range.
3. Custom Range:<br/>
Use custom values as the minimum and maximum of the visible range.

*Time-Series* that share the scale of another *Time-Series* will be displayed slightly indented underneath it.
[![Frontend Scales](/assets/images/frontend-scales.png)](/assets/images/frontend-scales.png)

Up to two different scales will be displayed as Y-axes.<br/>
Adding more scales will still result in correct visualization, but not show additional Y-axes. Which *Time-Series* are displayed on which Y-axis can be determined by the axis indicator in their top right corner. A missing indicator means that this *Time-Series* is using a scale that is not shown as Y-axis. 


## Selecting the Time Window

All *Time-Series* are using the same *Time Window*.<br/>
It consists of a period of time and a unit in which this period is to be subdivided.<br/>
The period defines which *Events* are considered from each *Time-Series*. The unit defines the number of individual data points for each *Time-Series*, using the *Aggregation* to combine the values from the existing *Events*.

[![Frontend Time Window](/assets/images/frontend-time-window.png)](/assets/images/frontend-time-window.png)

``Shortcuts`` are for common use cases and select both the period and the unit.<br/>
``Dynamic`` represents *Time Windows* relative to the current time and allow to select the period and unit independently.<br/>
``Custom`` allows fine grained control of every aspect of the *Time Window*.


## Chart Controls

The top right corner of the chart contains the controls for the automatic and manual refresh of the *Time-Series* and the chart type selection.
[![Frontend Chart Controls](/assets/images/frontend-chart-controls.png)](/assets/images/frontend-chart-controls.png)
The chart title can be changed by clicking on it.

## Export and Import

The current configuration of the chart can be exported and download as JSON configuration file.
This file can later be imported to restore your *Time-Series*.
[![Frontend Export Import](/assets/images/frontend-export-import.png)](/assets/images/frontend-export-import.png) 