---
layout: default
title: Event API
---
# Event API

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
  curl 'http://<logspace-hq.server>:4567/events' \
    -X PUT \
    -d @request.json \
    --header "Content-Type:application/json" \
    --header "logspace.space-token:development"
  ```

Example content of request.json

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
