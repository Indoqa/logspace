---
layout: default
title: Query API
---
# Query API

[Retrieve Suggestions](#retrieve-suggestions) <br/>
[Retrieve Time-Series](#retrieve-time-series)

## Retrieve Suggestions

Retrieve suggestions for *Spaces*, *Systems*, *Properties* and *Agents* that can be used for *Time-Series*.

<table>
  <tbody>
    <tr>
      <td>URL</td>
      <td>/suggestion</td>
    </tr>
    <tr>
      <td>Method</td>
      <td><code>POST</code></td>
    </tr>
    <tr>
      <td>URL Parameters</td>
      <td><code>None</code></td>
    </tr>
  </tbody>
</table>

### Header Parameters
`None`

### Body

```json
{ 
  "text" : "Search for partial matches in the Properties, Agents, Systems and Spaces in the Event Store.",
  "propertyId" : "Only consider Events having a Property with exactly this ID.",
  "systemId" : "Only consider Events having a System with exactly this ID.",
  "spaceId" : "Only consider Events having a Space with exactly this ID."
}
```

## Success Response

### Header
<table>
  <tbody>
    <tr>
      <td style="white-space: nowrap">Response Code:</td>
      <td>200 OK</td>
    </tr>
  </tbody>
</table>

### Body

The suggestion will contain all *Spaces*, *Systems*, *Properties* and *Agents* matching the given input.
  
```json
{
  "spaces" : [
    { 
      "id" : "ID of the Space (e.g. development)",
      "name" : "Name of the Space (e.g. development)"
    }
  ],
  "systems" : [
    {
      "id" : "ID of the System (e.g. localhost)",
      "name" : "Name of the System (e.g. localhost)"
    }
  ],
  "propertyNames" : [
    {
      "id" : "ID of the Property (e.g. double_property_system_cpu_load)",
      "name" : "Name of the Property (e.g. system_cpu_load)"
    }
  ],
  "agentDescriptions" : [
  {
      "globalId" : "Global ID of the Agent (e.g. development|localhost|os/cpu)",
      "name" : "name of the Agent (e.g os/cpu)",
      "space" : "Space of the Agent",
      "system" : "System of the Agent",
      "propertyDescriptions" : [
        {
          "id" : "ID of the Property (e.g. integer_property_processor_count)",
          "name" : "Name of the Property (e.g. processor_count) ",
          "propertyType": "[BOOLEAN | DATE | INTEGER | LONG | FLOAT | DOUBLE | STRING]",
          "units": [
            {
              "name" : "Name of the unit",
              "factor" : "Conversion factor"
            }
          ]
        }
      ]
    }
  ]
}
```

## Sample Call
This call will retrieve all *Spaces*, *Systems*, *Properties* and *Agents* in the *Event Store*.

```
curl 'http://localhost:4567/suggestion' \
    -X POST \
    -d @request.json \
    --header "Content-Type:application/json" 
```

``request.json``

```json
{
}
```
        
This call will retrieve all *Spaces*, *Systems*, *Properties* and *Agents* in the *Event Store*, where the *Space* is `development`

```
curl 'http://localhost:4567/suggestion' \
    -X POST \
    -d @request.json \
    --header "Content-Type:application/json" 
```

``request.json``

```json
{
  "spaceId" : "development"
}
```        
  
## Retrieve Time-Series

Retrieve the aggregated data for one or more *Time-Series*.

<table>
  <tbody>
    <tr>
      <td>URL</td>
      <td>/time-series</td>
    </tr>
    <tr>
      <td>Method</td>
      <td><code>POST</code></td>
    </tr>
    <tr>
      <td>URL Parameters</td>
      <td><code>None</code></td>
    </tr>
  </tbody>
</table>

### Header Parameters
`None`

### Body
  
```json
{ 
  "definitions" : [
    { 
      "timeWindow" : 
        {
          "start" : "The start of the Time Window in the format 'YYYY-MM-ddTHH:mm:ssZ'",
          "end" : "The end of the Time Window in the format 'YYYY-MM-ddTHH:mm:ssZ'",          
          "gap" : "The gap of the Time Window in seconds."
        },
      "globalAgentId" : "The global ID of the Agent.",
      "propertyId" : "The ID of the EventProperty.",
      "aggregate" : "Aggregate function: [max | min | avg | count | sum]"
    }
  ]
}
```


## Success Response

### Header
Response code: `200 OK`

### Body

The values of the requested *Time-Series*.
  
```json
{
  "timeWindow" : 
  {
    "start" : "The start of the Time Window in the format 'YYYY-MM-ddTHH:mm:ssZ'",
    "end" : "The end of the Time Window in the format 'YYYY-MM-ddTHH:mm:ssZ'",          
    "gap" : "The gap of the Time Window in seconds."
  },
  "data":
  [
    ["A", "B", "C", "D"]
  ]
}
```

## Sample Call

```
curl 'http://localhost:4567/time-series' \
    -X POST \
    -d @request.json \
    --header "Content-Type:application/json"
```

``request.json``
  
```json
{
  "definitions": [
    {
      "timeWindow":
      {
        "start" : "2015-01-01T00:00:00Z", 
        "end" : "2016-01-01T00:00:00Z", 
        "gap" : 604800
      }, 
      "globalAgentId" : "development|localhost|os/cpu", 
      "propertyId" : "double_property_system_cpu_load", 
      "aggregate" : "avg"
    }
  ]
}
```

> You will have to adjust the Global Agent ID to an actually existing ID to retrieve any data. Such an ID can be obtained by retrieving suggestions.