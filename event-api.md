---
layout: default
title: Event API
---
# Event API

[Store Events](#store-events)<br/>
[Retrieve Events](#retrieve-events)

## Store Events

Stores multiple events in the *Logspace HQ*.

<table>
  <tbody>
    <tr>
      <td>URL</td>
      <td>/events</td>
    </tr>
    <tr>
      <td>Method</td>
      <td><code>PUT</code></td>
    </tr>
    <tr>
      <td>URL Parameters</td>
      <td><code>None</code></td>
    </tr>
  </tbody>
</table>

### Header Parameters
<table>
  <tbody>
    <tr>
      <td style="white-space: nowrap">logspace.space-token=[space-token]</td>
      <td>This header serves as an authentication token for the caller to use this API.
The supplied <a href="/configuration-hq-spaces">Space-Token</a> must be valid.</td>
    </tr>
  </tbody>
</table>

### Body

```json
[
  {
    "id" : "Unique ID for this Event",
    "type" : "Specific type of this Event",
    "system" : "System identifier",
    "agent-id" : "The ID of the Agent that produced this Event",
    "timestamp" : "Timestamp of the creation of this event",
    "pid" : "The optional parent Event ID",
    "gid" : "The optional global Event ID",

    "boolean-properties" : { "property_name" : "boolean-value" },
    "date-properties"    : { "property_name" : "date-value" },
    "double-properties"  : { "property_name" : "double-value" },
    "float-properties"   : { "property_name" : "float-value" },
    "integer-properties" : { "property_name" : "integer-value" },
    "long-properties"    : { "property_name" : "long-value" },
    "string-properties"  : { "property_name" : "string-value" }
  }
]
```

## Success Response

### Header
<table>
  <tbody>
    <tr>
      <td style="white-space: nowrap">Response Code:</td>
      <td>202 Accepted</td>
    </tr>
  </tbody>
</table>

### Body
`none`

## Error Response

### Header
<table>
  <tbody>
    <tr>
      <td style="white-space: nowrap">Response Code:</td>
      <td>403 Forbidden</td>
    </tr>
  </tbody>
</table>

### Body
If the request did not contain the *Space-Token* header

```json
{
  "type" : "MISSING_SPACE_TOKEN",
  "message" : "Missing header 'logspace.space-token'."
}
```

If the supplied *Space-Token* is invalid:

```json
{
  "type" : "INVALID_SPACE_TOKEN",
  "message" : "Unrecognized space-token '{provided space-token}'.",
  "parameters" :
    {
      "space-token": "{provided space-token}"
    }
}
```


## Sample Call

  ```
  curl 'http://localhost:4567/events' \
    -X PUT \
    -d @request.json \
    --header "Content-Type:application/json" \
    --header "logspace.space-token:development"
  ```

``request.json``

```json
[
  {  
    "id" : "bbcca991-1070-4325-b719-dc9014816a2e",
    "type" : "os/cpu",
    "system" : "localhost",
    "agent-id" : "os/cpu",
    "timestamp" : "2015-05-18T14:07:00Z",
    "double-properties" :
    {
      "system_cpu_load" : 0.44324237001029815
    },
    "integer-properties" :
    {  
      "processor_count" : 4
    }
  },
  {  
    "id" : "a00f812e-ea08-4d38-9ce0-fa5973f0d411",
    "type" : "os/memory",
    "system" : "localhost",
    "agent-id" : "os/memory",
    "timestamp" : "2015-05-18T14:07:00Z",
    "long-properties" :
    {  
      "total_memory" : 16490561536,
      "free_memory" : 2074169344,
      "used_memory" : 14416392192,
      "committed_virtual_memory" : 5659140096
    }
  }
]
```

## Retrieve Events

Retrieve *Events* from the *Event Store*.

<table>
  <tbody>
    <tr>
      <td>URL</td>
      <td>/events</td>
    </tr>
    <tr>
      <td>Method</td>
      <td><code>POST</code></td>
    </tr>
    <tr>
      <td>URL Parameters</td>
      <td><code>count</code>: The maximum number of Events to retrieve; Default is 10<br/><code>cursor</code>: The position from which to retrieve Events; Default is *</td>
    </tr>
  </tbody>
</table>

Paging is done using a cursor.<br/>
To retrieve the next page of *Events* the "nextCursorMark" from the last result must be provided as the "cursor" for the next request.
If the "cursor" is missing or "*", retrieval will start from the beginning.

### Header Parameters
`None`
 
### Body
The Event Filter

```json
{
  "elements" : [{
      "property" : "Property ID",
      "value" : "Property value"
    },
    {
      "property" : "Property ID",
      "values" : ["Property value", "Property value", "Property value"],
      "operator" : "OR"
    },
    {
      "property" : "Property ID",
      "from" : "Property value",
      "to" : "Property value"
    }
  ]
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

```json
{
  "totalCount" : 225337,
  "nextCursorMark" : "AoJxrKnjitECPwU2NGM0ZDQ5OC1jMTQ3LTQ0MDctYjU4Mi1mNDIyMGQ3NDRjN2I=",
  "events" : [ {
    "id" : "028cf85f-b38b-40c7-a038-781025cf0502",
    "type" : "os/memory",
    "system" : "S430",
    "agent-id" : "os/memory",
    "timestamp" : "2015-11-17T13:05:55.001Z",
    "long-properties" : {
      "total_memory" : 16754085888,
      "free_memory" : 9407488000,
      "used_memory" : 7346597888,
      "committed_virtual_memory" : 133668864
    }
  }]
}
```