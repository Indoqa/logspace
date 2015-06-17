/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import Immutable from 'immutable'
import axios from 'axios'

import {register,waitFor} from '../dispatcher'

import * as timeWindowActions from '../time-window/actions'
import * as timeSeriesActions from '../time-series/actions'
import * as resultActions from './actions'

import {getRestUrl} from '../rest'
import {transformLogspaceResult} from './c3js-chartplugin'

import {resultCursor, timeSeriesCursor, timeWindowCursor} from '../state'

import {TimeWindowStore_dispatchToken, getTimeWindow} from '../time-window/store'
import {TimeSeriesStore_dispatchToken, getTimeSeries} from '../time-series/store'

export const ResultStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case timeWindowActions.selectCustomDate:
      waitFor([TimeWindowStore_dispatchToken])
      refreshResult()
      break

    case timeWindowActions.selectPredefinedDate:
      waitFor([TimeWindowStore_dispatchToken])
      refreshResult()
      break  

    case timeWindowActions.selectDynamicDate:
      waitFor([TimeWindowStore_dispatchToken])
      refreshResult()
      break    

    case timeSeriesActions.onTimeSeriesSaved:
      waitFor([TimeSeriesStore_dispatchToken])
      refreshResult()
      break

    case timeSeriesActions.onTimeSeriesDeleted:
      waitFor([TimeSeriesStore_dispatchToken])
      refreshResult()
      break

    case resultActions.refreshResult:
      refreshResult()
      break

    case resultActions.saveChartTitle:
      resultCursor(result => {
        return result.set('chartTitle', data)
      })
      break

    case resultActions.setChartType:
      resultCursor(result => {
        return result.set('chartType', data)
      })
      break  
  }
})

function refreshResult() {
  var timeSeries = timeSeriesCursor()
  var timeWindow = timeWindowCursor()

  if (timeSeries.isEmpty()) {
    storeEmptyResult()
    return
  }

  storeLoadingResult()

  axios.post(getRestUrl('/query'), createRestRequest(timeSeries, timeWindow))
  .then(function (response) {
    storeSuccessResult(timeSeries, response.data, timeWindow)
  })
  .catch(function (response) {
    console.log(response)
    storeErrorResult(response)
  })
}


function createRestRequest(timeSeries, timeWindow) {
  var request = {
    'dataDefinitions': []
  }

  timeSeries.forEach(function(item) {
    request.dataDefinitions.push({
      'dateRange': {
        'start': timeWindow.get('selection').start(),
        'end': timeWindow.get('selection').end(),
        'gap': timeWindow.get('selection').get('gap').get('amount') * timeWindow.get('selection').get('gap').get('unit').get('factor')
      },
      'globalAgentId': item.get('agentId'),
      'propertyId': item.get('propertyId'),
      'aggregate': item.get('aggregate')
    })
  })

  return request
}

function storeEmptyResult() {
  resultCursor(result => {
    return result.set('translatedResult', Immutable.fromJS({
      empty: true,
      loading: false,
      error: false
    }))
  })
}

function storeLoadingResult() {
  resultCursor(result => {
    return result.set('translatedResult', Immutable.fromJS({
      empty: false,
      loading: true,
      error: false
    }))
  })
}

function storeErrorResult(serverResponse) {
  resultCursor(result => {
    return result.set('translatedResult', Immutable.fromJS({
      empty: true,
      error: true,
      loading: false,
      errorStatus: serverResponse.status,
      errorText: serverResponse.statusText
    }))
  })
}

function storeSuccessResult(timeSeries, responseJson, timeWindow) {
  const chartData = transformLogspaceResult(timeSeries, responseJson)

  resultCursor(result => {
    return result.set('translatedResult', Immutable.fromJS( {
      empty: false,
      error: false,
      loading: false,
      chartData: chartData,
      gap: timeWindow.get('selection').get('gap')
    }))
  })
}
