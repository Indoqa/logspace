---
layout: default
title: Native Query API
---
# Native Query API

[Native Query (POST)](#native-query-(post))<br/>
[Native Query (GET)](#native-query-(get))

## Native Query (POST)

Execute a query directly against the *Event Store* using HTTP POST.
The query parameters are provided in the body of the request.

<table>
  <tbody>
    <tr>
      <td>URL</td>
      <td>/native-query</td>
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

The body contains the parameters that are forwarded directly to the *Event Store*.

```json
{
  "parameters" : {
    "parameter1" : ["value", "value"],
    "parameter2" : ["value"],
    "parameter3" : ["value", "value", "value"]
  }
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
The response from *Event Store*

## Sample Call

This is a sample request that works with Apache Solr as the *Event Store*.

  ```
  curl 'http://localhost:4567/native-query' \
    -X POST \
    -d @request.json \
    --header "Content-Type:application/json"
  ```

``request.json``

```json
{
  "parameters" : {
    "q" : ["*:*"],
    "rows" : ["0"],
    "facet" : ["true"],
    "facet.field" : ["global_agent_id"]
  }  
}
```

## Native Query (GET)

Execute a query directly against the *Event Store* using HTTP GET.
The query parameters are provided in the query string of the request.

<table>
  <tbody>
    <tr>
      <td>URL</td>
      <td>/native-query</td>
    </tr>
    <tr>
      <td>Method</td>
      <td><code>GET</code></td>
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
`None`

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
The response from *Event Store*

## Sample Call

This is a sample request that works with Apache Solr as the *Event Store*.

```
curl 'http://localhost:4567/native-query?q=*:*&rows=0&facet=true&facet.field=global_agent_id' \
  -X GET \
  --header "Content-Type:application/json"
```
  