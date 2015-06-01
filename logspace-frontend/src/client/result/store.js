/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import Immutable from 'immutable'
import axios from 'axios'
import moment from 'moment'

import {resultCursor} from '../state'
import {register,waitFor} from '../dispatcher'

import * as timeWindowActions from '../time-window/actions'
import * as timeSeriesActions from '../time-series/actions'
import * as resultActions from './actions'

import {getRestUrl} from '../rest'
import {TimeWindowStore_dispatchToken, getTimeWindow} from '../time-window/store'
import {TimeSeriesStore_dispatchToken, getTimeSeries} from '../time-series/store'

export function getResult() {
  return resultCursor().get("translatedResult")
}

export const ResultStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case timeWindowActions.onTimeWindowChange:
      waitFor(TimeWindowStore_dispatchToken)
      refreshResult()
      break

    case timeSeriesActions.onTimeSeriesSaved:
      waitFor(TimeSeriesStore_dispatchToken)
      refreshResult()
      break

    case timeSeriesActions.onTimeSeriesDeleted:
      waitFor(TimeSeriesStore_dispatchToken)
      refreshResult()
      break

    case resultActions.onResultRefreshed:
      refreshResult()
      break
  }
})

function refreshResult() {
  var timeSeries = getTimeSeries()
  var timeWindow = getTimeWindow()

  if (timeSeries.isEmpty()) {
    storeEmptyResult()
    return
  }

  storeLoadingResult()

  axios.post(getRestUrl('/query'), createRestRequest(timeSeries, timeWindow))
  .then(function (response) {
    storeSuccessResult(timeSeries, response.data)
  })
  .catch(function (response) {
    console.log(response)
    storeErrorResult(response)
  })
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
      "globalAgentId": item.get("agentId"),
      "propertyId": item.get("propertyId"),
      "aggregate": item.get("aggregate")
    })
  })

  return request
}

function storeEmptyResult() {
  resultCursor(result => {
    return result.set("translatedResult", Immutable.fromJS({
      empty: true,
      loading: false,
      error: false
    }))
  })
}

function storeLoadingResult() {
  resultCursor(result => {
    return result.set("translatedResult", Immutable.fromJS({
      empty: false,
      loading: true,
      error: false
    }))
  })
}

function storeErrorResult(serverResponse) {
  resultCursor(result => {
    return result.set("translatedResult", Immutable.fromJS({
      empty: true,
      error: true,
      loading: false,
      errorStatus: serverResponse.status,
      errorText: serverResponse.statusText
    }))
  })
}

function storeSuccessResult(timeSeries, responseJson) {
  const xvalues = createXAxisLabals(responseJson)
  const originalColumns = {}
  const columns = []
  const columnKeys = []
  const colors = {}
  const names = {}
  const types = {}
  const dataAxes = {}
  const warnings = []
  const axisRanges = {
    min: {y:0, y2:0},
    max: {y:0, y2:0}
  };
  
  timeSeries.forEach(function(item, index) {
      // meta data
      columnKeys.push(item.get("id"))
      colors[item.get("id")] = item.get("color")
      names[item.get("id")] = item.get("name") + ': ' + item.get("aggregate") + " " + item.get("propertyId")

      // type
      const typeArray = types[item.get("type")];
      if (typeArray == null) {
        types[item.get("type")] = [];
      }

      types[item.get("type")].push(item.get("id"));

      // apply to scale
      const scaleMin = parseInt(item.get("scaleMin"))
      const scaleMax = parseInt(item.get("scaleMax"))
      let normalizer;

      if (axisRanges.max.y == 0) {
        axisRanges.min.y = scaleMin
        axisRanges.max.y = scaleMax 
        dataAxes[item.get("id")] = 'y'; 
        timeSeriesActions.onAxisChanged(item.get("id"), 'y1')

      } else if (axisRanges.min.y == scaleMin && axisRanges.max.y == scaleMax)  {
        dataAxes[item.get("id")] = 'y'
        timeSeriesActions.onAxisChanged(item.get("id"), 'y1')

      } else if (axisRanges.max.y2 == 0) {
        axisRanges.min.y2 = scaleMin
        axisRanges.max.y2 = scaleMax
        dataAxes[item.get("id")] = 'y2'
        timeSeriesActions.onAxisChanged(item.get("id"), 'y2')

      } else if (axisRanges.min.y2 == scaleMin && axisRanges.max.y2 == scaleMax)  {
        dataAxes[item.get("id")] = 'y2'
        timeSeriesActions.onAxisChanged(item.get("id"), 'y2')

      } else {
        dataAxes[item.get("id")] = 'y'
        timeSeriesActions.onAxisChanged(item.get("id"), 'y!')  

        normalizer = (value) => {
          const onePercentOfOriginal = (scaleMax - scaleMin) / 100
          const percentOfOriginal = value / onePercentOfOriginal
          const targetRange = axisRanges.max.y - axisRanges.min.y
          const onePercentOfTarget = targetRange / 100
          return onePercentOfTarget * percentOfOriginal
        }
      }

      // set y values
      columns.push(getDataColumn(responseJson.data[index], item.get("id"), normalizer))
      originalColumns[item.get("id")] = responseJson.data[index]
  })

  // data is stored in c3js ready format, see http://c3js.org/reference.html#api-load
  var xdata = xvalues.slice();
  xdata.unshift('x')
  columns.unshift(xdata)
  
  const chartData = {
      colors: colors,
      columnKeys: columnKeys,
      columns: columns,
      originalColumns: originalColumns,
      axes: dataAxes
  }

  resultCursor(result => {
    return result.set("translatedResult", Immutable.fromJS( {
      empty: false,
      error: false,
      loading: false,
      axisRanges: axisRanges,
      types: types,
      xvalues: xvalues,
      xgap: responseJson.dateRange.gap,
      names: names,
      chartData: chartData,
      warnings: warnings
    }))
  })
}

function createXAxisLabals(responseJson) {
  let labels = []

  const start = new Date(responseJson.dateRange.start).getTime()
  const end = new Date(responseJson.dateRange.end).getTime()
  const gap = responseJson.dateRange.gap

  for (let i = start; i < end; i = i + (gap * 1000)) {
    labels.push(moment.utc(new Date(i)))
  }

  return labels
}


function getDataColumn(array, id, normalizer) {
  var values =  array.map(function(item) {
     if (item != null && normalizer != null) {
       return normalizer(item)
     }

     if (item != null) {
       return item
     }

     return null
   })

   // add id as first value of data array, see http://c3js.org/reference.html#data-columns
   values.unshift(id)

  return values
}
