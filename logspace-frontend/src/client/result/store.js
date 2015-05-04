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
import {resultCursor} from '../state';
import {register,waitFor} from '../dispatcher';
import {TimeWindowStore_dispatchToken, getTimeWindow} from '../time-window/store';
import {TimeSeriesStore_dispatchToken, getTimeSeries} from '../time-series/store';

export function getResult() {
  return resultCursor().get("restResult");
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
  }
});  

function refreshResult() {
  var request = {
    "dataDefinitions": []
  }
  
  var series = getTimeSeries();
  var timeWindow = getTimeWindow();
  
  series.forEach(function(item) { 
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

  axios.post('/query', request)
  .then(function (response) {
    resultCursor(result => {
      return result.set("restResult", Immutable.fromJS(response.data));
    });
  })
  .catch(function (response) {
    console.log(response);
  });
}
