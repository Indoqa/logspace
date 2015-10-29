---
layout: default
title: Event API
---
# Event API

## Store Events

Stores multiple events in Logspace.

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
The supplied space-token must be configured in the Logspace HQ to be accepted.</td>
    </tr>
  </tbody>
</table>


### Body

```json
[
  {
    "id" : "Unique ID for this event (e.g. UUID)",
    "type" : "Specific type of this event (e.g. os/cpu)",
    "system" : "System identifier (e.g. hostname)",
    "agent-id" : "Agent ID (e.g. hostname/cpu)",
    "timestamp" : "Timestamp of the creation of this event (e.g. 2015-05-21T11:30:00Z)",
    "pid" : "Optional Parent Event ID",
    "gid" : "Optional Global Event ID",

    "boolean-properties" : { "property_name" : "boolean-value [true|false]" },
    "date-properties"    : { "property_name" : "date-value [YYYY-MM-ddTHH:mm:ssZ]" },
    "double-properties"  : { "property_name" : "double-value [json number]" },
    "float-properties"   : { "property_name" : "float-value [json number]" },
    "integer-properties" : { "property_name" : "integer-value [json number]" },
    "long-properties"    : { "property_name" : "long-value [json number]" },
    "string-properties"  : { "property_name" : "string-value [json string]" }
  }
]
```

## Success Response

### Header
Response code: `202 Accepted`

### Body
`none`

## Error Response

### Header
Response code: `403 Forbidden`

### Body
If the request did not contain the space-token header

```json
{
  "type" : "MISSING_SPACE_TOKEN",
  "message" : "Missing header 'logspace.space-token'."
}
```
If the supplied space-token is not configured:

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
