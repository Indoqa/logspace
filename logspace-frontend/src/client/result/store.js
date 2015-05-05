/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import Immutable from 'immutable';
import axios from 'axios';
import * as timeWindowActions from '../time-window/actions';
import * as timeSeriesActions from '../time-series/actions';
import * as resultActions from './actions';
import {resultCursor} from '../state';
import {register,waitFor} from '../dispatcher';
import {getRestUrl} from '../rest';
import {TimeWindowStore_dispatchToken, getTimeWindow} from '../time-window/store';
import {TimeSeriesStore_dispatchToken, getTimeSeries} from '../time-series/store';

export function getResult() {
  return resultCursor().get("translatedResult");
}

export const ResultStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case timeWindowActions.onTimeWindowChange:
      waitFor(TimeWindowStore_dispatchToken);
      refreshResult();  
      break;
      
    case timeSeriesActions.onTimeSeriesAdded:
      waitFor(TimeSeriesStore_dispatchToken);
      refreshResult();  
      break;  
      
    case timeSeriesActions.onTimeSeriesDeleted:
      waitFor(TimeSeriesStore_dispatchToken);
      refreshResult();  
      break;    

    case resultActions.onResultRefreshed:
      refreshResult();  
      break;      
  }
});  

function refreshResult() {
  var timeSeries = getTimeSeries();
  var timeWindow = getTimeWindow();

  if (timeSeries.isEmpty()) {
    storeEmptyResult()
    return;
  }

  axios.post(getRestUrl('/query'), createRestRequest(timeSeries, timeWindow))
  .then(function (response) {
    storeSuccessResult(timeSeries, response.data)  
  })
  .catch(function (response) {
    console.log(response)
    storeErrorResult(response)  
  });
}


function createRestRequest(timeSeries, timeWindow) {
  var request = {
    "dataDefinitions": []
  }
  
  timeSeries.forEach(function(item) { 
    request.dataDefinitions.push({
      "dateRange": {
            "start": timeWindow.get('start'),
            "end": timeWindow.get('end'),
            "gap": timeWindow.get('gap')
      },
      "space": "development",
      "agentId": item.get("agentId"),        
      "propertyId": item.get("propertyId"),
      "aggregate": item.get("aggregate")     
    });
  })

  return request;  
}

function storeEmptyResult() {
  resultCursor(result => {
    return result.set("translatedResult", Immutable.fromJS({
      empty: true,
      error: false
    }));
  });  
}

function storeErrorResult(serverResponse) {
  resultCursor(result => {
    return result.set("translatedResult", Immutable.fromJS({
      empty: true,
      error: true,
      errorStatus: serverResponse.status,
      errorText: serverResponse.statusText 
    }));
  });
}

function storeSuccessResult(timeSeries, responseJson) {
  var chartData = {
      labels: createXAxisLabals(responseJson),
      datasets: createDatasets(timeSeries, responseJson)
  }
 
  resultCursor(result => {
    return result.set("translatedResult", Immutable.fromJS( {
      empty: false,
      error: false,
      chartData: chartData
    }));
  });
} 

function createXAxisLabals(responseJson) {
  var labels = [];

  var start = new Date(responseJson.dateRange.start).getTime();
  var end = new Date(responseJson.dateRange.end).getTime();
  var gap = responseJson.dateRange.gap;
 
  for (var i = start; i < end; i = i + gap) {
    labels.push(new Date(i));
  }
  
  return labels;  
}

function createDatasets(timeSeries, responseJson) {
  var datasets = [];
  
  {timeSeries.forEach(function(item, index) {
      datasets.push({
          label: item.get("id"),
          fillColor: "transparent",
          strokeColor: item.get("color"),
          pointColor: item.get("color"),
          pointHighlightStroke: "#FFF",
          data: fillNullValues(responseJson.data[index])
      });  
  })}  

  return datasets;
}

function fillNullValues(array) {
  return array.map(function(item) {
     if (item != null) {
       return item; 
     }

     return 0;
   });  
} 

