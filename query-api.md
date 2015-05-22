---
layout: default
title: Query API
---
#Query API

* [Retrieve Suggestions](#retrieve-suggestions)
* [Retrieve and Aggregate Stored Data](#retrieve-and-aggregate-stored-data)

##Retrieve Suggestions

  Retrieve suggestions for information in stored events like *properties*, *systems* or *spaces*.
  The search term in the field `text` provides partial matching against all information in the stored events, where `propertyId`, `systemId` and `spaceId` allow to filter the suggestions, but the values must be known in advanced no partial matching is provided.

* **URL:** /suggest

* **Method:**
  `POST`
  
*  **URL Parameters:** None

* **Header Parameters:** None

* **Data Parameters:**

```json
{ 
  "text" : "Search for given literal in the propertyId, agentId, system 
              or space field of stored events.",
  "propertyId" : "Filter the result by the given propertyId.",
  "systemId" : "Filter the result by the given systemId.",
  "spaceId" : "Filter the result by the given spaceId."
}
```

* **Success Response:** <br/>
  The suggestion result will return all information about the stored events matching the search or filter criteria by the given input.
  
    * **Code:** 200 OK
  
        * **Content:** The suggestion result.
  
        ```json
        {
          "spaces" : [
            { 
              "id" : "id of the space (e.g. development)",
              "name" : "name of the space (e.g. development)"
            }
          ],
          "systems" : [
            {
              "id" : "id of the system (e.g. localhost)",
              "name" : "name of the system (e.g. localhost)"
            }
          ],
          "propertyNames" : [
            {
              "id" : "id of the property (e.g. double_property_system_cpu_load)",
              "name" : "name of the property (e.g. system_cpu_load)"
            }
          ],
          "agentDescriptions" : [
          {
              "globalId" : "globalId of the agent (e.g. integrationtest|localhost|dev/cpu)",
              "name" : "name of the agent (e.g dev/cpu)",
              "space" : "space (e.g. development)",
              "system" : "system (e.g. localhost)",
              "propertyDescriptions" : [
	        {
	          "id" : "Id of the property (e.g. string_property_query)",
	          "name" : "Name of the property (e.g. query) ",
	          "propertyType": "[BOOLEAN | DATE | INTEGER | LONG | FLOAT | DOUBLE | STRING] (e.g. STRING)",
	          "units": [
	            {
	              "name" : "Name of the factor",
	              "factor" : "Double-value of the factor"
	            }
	          ]
	        }
              ]
            }
          ]
        }
        ```


* **Error Response:**


* **Sample Call:**

    * This call will retrieve all *spaces*, *systems*, *propertyNames* and *agentDescriptions* found in the stored events.

        ```
        curl 'http://<logspace-hq.server>:4567/suggest' -X POST -d @request.json --header "Content-Type:application/json" 
        ```

           request.json
  
        ```json
        {
          "text" : ""
        }
        ```
        
    * This call will retrieve all *spaces*, *systems*, *propertyNames* and *agentDescriptions* found in the stored events,
      where the *space* is `development`

        ```
        curl 'http://<logspace-hq.server>:4567/suggest' -X POST -d @request.json --header "Content-Type:application/json" 
        ```

           request.json
  
        ```json
        {
          "spaceId" : "development"
        }
        ```        
  
  
##Retrieve and Aggregate Stored Data

  Retrieve and aggregate stored properties of agents. The retrieval can be restricted by multiple `dateRanges` with an `start` and `end` point and also a `gap`.

* **URL:** /query

* **Method:**
  `POST`
  
*  **URL Parameters:** None

* **Header Parameters:** None

* **Data Parameters:**
  
  Multiple dataDefinitions can be provided to query for different data in one request.

```json
{ 
  "dataDefinitions" : [
    { 
      "dateRange" : 
        {
          "start" : "Start timestamp for retrieval of stored events. date-value [YYYY-MM-ddTHH:mm:ssZ]",
          "end" : "End timestamp for retrieval of stored events. date-value [YYYY-MM-ddTHH:mm:ssZ]",          
          "gap" : "Gap in seconds. integer-value [json number]"
        },
      
      "globalAgentId" : "The global agent id for which the data schould be retrieved.",
      "propertyId" : "The id of the property on which the retrieval and aggregation should be done. Must be a property of the given agent.",
      "aggregate" : "Aggregate function: [max | min | avg | count | sum]"
    }
  ]
}
```

* **Success Response:**
  
    * **Code:** 200 OK
  
        * **Content:** The suggestion result.
  
        ```json
        {
          "dateRange" : 
          {
            "start" : "Start point of the given dataDefinition.",
            "end" : "End point of the given dataDefinition.",
            "gap" : "Gap of the given dataDefinition." 
          },
          
          "data":
          [
            ["retrieved data", "for each", "dataDefinition"]
          ]
        }
        ```

* **Error Response:**


* **Sample Call:**

  For this call you have to **know** the `globalAgentId` and the `propertyId` you want to query for.
  Use the /suggest API with the sample call provided earlier to get matching data for your stored events.
  The property you want to query for must be defined for the agent you provided via the `globalAgentId`. The properties the agent stores are described in the `agentDescription`.

  ```
  curl 'http://<logspace-hq.server>:4567/query' -X POST -d @request.json --header "Content-Type:application/json"
  ```

  request.json
  
```json
{
  "dataDefinitions": [
    {
      "dateRange":
      {
	  "start":"Start time of stored events (e.g. 2015-05-19T11:13:00)", 
	  "end":"End time of stored events (e.g. 2015-05-19T11:20:00)", 
	  "gap": 15
      }, 
      
      "globalAgentId":"The globalAgentId, take one you find via the /suggest API", 
      "propertyId":"The id of the property you want to query for, must be declared in the agentDescription for the globalId", 
      "aggregate":"count"
    }
  ]
}
```  