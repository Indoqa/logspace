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

import {register,waitFor} from '../dispatcher'

import * as timeWindowActions from '../time-window/actions'
import * as timeSeriesActions from '../time-series/actions'
import * as resultActions from './actions'

import {restUrl} from '../environment'
import {transformLogspaceResult} from './c3js-chartplugin'

import {resultCursor, timeSeriesCursor, timeWindowCursor} from '../state'

import {TimeWindowStore_dispatchToken, getTimeWindow} from '../time-window/store'
import {TimeSeriesStore_dispatchToken, getTimeSeries} from '../time-series/store'

let nextAutoPlay = null

export const ResultStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case timeWindowActions.selectCustomDate:
      waitFor([TimeWindowStore_dispatchToken])
      setAutoPlay(false)
      refreshResult()
      break

    case timeWindowActions.selectPredefinedDate:
      waitFor([TimeWindowStore_dispatchToken])
      setAutoPlay(false)
      refreshResult()
      break  

    case timeWindowActions.selectDynamicDate:
      waitFor([TimeWindowStore_dispatchToken])
      setAutoPlay(true)
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

    case resultActions.setAutoPlay:
      setAutoPlay(data)

      if (data) {
        refreshResult()  
      } else {
        clearAutoPlay()  
      }

      break  
  }
})

function setAutoPlay(enabled) {
  resultCursor(result => {
    return result.set('autoPlay', enabled)
  })
}

function refreshResult() {
  clearAutoPlay()  

  var timeSeries = timeSeriesCursor()
  var timeWindow = timeWindowCursor()

  if (timeSeries.isEmpty()) {
    storeEmptyResult()
    return
  }

  storeLoadingResult()

  axios.post(restUrl('/query'), createRestRequest(timeSeries, timeWindow))
  .then(function (response) {
    storeSuccessResult(timeSeries, response.data, timeWindow)
  })
  .catch(function (response) {
    console.error(response)
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

  triggerNextAutoPlay()
}

function storeLoadingResult() {
  resultCursor(result => {
    return result.set('translatedResult', Immutable.fromJS({
      empty: false,
      loading: true,
      error: false
    }))
  })

  resultCursor(result => {
    return result.set('autoPlaySchedule', null)
  })
}

function storeErrorResult(serverResponse) {
  const errorStatus = getErrorStatus(serverResponse)
  const errorMessage = getErrorMessage(serverResponse)

  resultCursor(result => {
    return result.set('translatedResult', Immutable.fromJS({
      empty: true,
      error: true,
      loading: false,
      lastUpdated: moment(),
      errorStatus: errorStatus,
      errorText: errorMessage
    }))
  })

  triggerNextAutoPlay()
}

function storeSuccessResult(timeSeries, responseJson, timeWindow) {
  const chartData = transformLogspaceResult(timeSeries, responseJson)

  resultCursor(result => {
    return result.set('translatedResult', Immutable.fromJS( {
      empty: false,
      error: false,
      loading: false,
      chartData: chartData,
      lastUpdated: moment(),
      gap: timeWindow.get('selection').get('gap')
    }))
  })

  triggerNextAutoPlay()
}

function triggerNextAutoPlay() {
  if (resultCursor().get('autoPlay')) {
    nextAutoPlay = setTimeout(refreshResult, 15000)

    resultCursor(result => {
      return result.set('autoPlaySchedule', moment())
    })
  }
}

function clearAutoPlay() {
  if(nextAutoPlay) {
    clearTimeout(nextAutoPlay)
  }
}

function getErrorStatus(serverResponse) {
  if (serverResponse.data && serverResponse.data.type) {
    return serverResponse.data.type
  }

  if (serverResponse.statusText) {
    return serverResponse.statusText
  }

  return ''
}

function getErrorMessage(serverResponse) {
  if (serverResponse.data && serverResponse.data.message) {
    return serverResponse.data.message
  }

  if (serverResponse.data) {
    return serverResponse.data
  }

  return ''
}
