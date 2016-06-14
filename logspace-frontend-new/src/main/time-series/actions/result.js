/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import moment from 'moment'
import {fetchLogspace} from '../../app/utils/fetchApis'
import {transformLogspaceResult} from './c3js-chartplugin'

export const REFRESH_RESULT = 'REFRESH_RESULT'
export const SAVE_CHART_TITLE = 'SAVE_CHART_TITLE'
export const SET_CHART_TYPE = 'SET_CHART_TYPE'
export const SET_AUTOPLAY = 'SET_AUTOPLAY'

const createRestRequest = (timeSeries, timeWindow) => {
  const request = {
    definitions: []
  }

  const selection = timeWindow.toJS()

  timeSeries.forEach((item) => {
    request.definitions.push({
      timeWindow: {
        start: selection.start().seconds(0).milliseconds(0),
        end: selection.end().seconds(59).milliseconds(999).add(1, 'milliseconds'),
        gap: selection.gap.amount * selection.gap.unit.factor
      },
      globalAgentId: item.get('agentId'),
      propertyId: item.get('propertyId'),
      aggregate: item.get('aggregate'),
      filter: {
        elements: item.get('filter').toJS().map((filter) => ({property: filter.id, value: filter.input}))
      }
    })
  })

  return request
}

export const refreshResult = () => ({store}) => {
  const timeSeries = store.getState().timeSeries.get('timeSeries')
  const timeWindow = store.getState().timeWindow.get('selection')

  if (timeSeries.isEmpty()) {
    return {
      type: REFRESH_RESULT,
      payload: Promise.resolve({lastUpdated: moment()})
    }
  }

  return {
    type: REFRESH_RESULT,
    payload: {
      promise: fetchLogspace('/time-series', {
        method: 'POST',
        body: JSON.stringify(createRestRequest(timeSeries, timeWindow)),
      })
      .then((json) => transformLogspaceResult(timeSeries, json))
      .then((transformedResult) => ({
        chartData: transformedResult,
        lastUpdated: moment(),
        gap: timeWindow.get('gap')
      }))
    }
  }
}

export const saveChartTitle = (title) => ({
  type: SAVE_CHART_TITLE,
  payload: {title}
})

export const setChartType = (type) => ({
  type: SET_CHART_TYPE,
  payload: {type}
})

export const setAutoPlay = (enabled) => ({
  type: SET_AUTOPLAY,
  payload: {enabled}
})
