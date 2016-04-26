/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import Immutable, {Record} from 'immutable'
import moment from 'moment'
import * as resultActions from '../actions/result'

const InitialState = Record({
  result: {
    translatedResult: {
      error: false,
      empty: true,
      series: null,
      xvalues: [],
      warnings: []
    },
    chartTitle: 'New Chart',
    chartType: 'spline',
    autoPlay: false
  }
})

export default (state = InitialState, action) => {
  switch (action.type) {
    case `${resultActions.REFRESH_RESULT}_START`: {
      state = state.set('translatedResult', Immutable.fromJS({
        empty: false,
        loading: true,
        error: false
      }))

      state = state.set('autoPlaySchedule', null)

      return state
    }

    case `${resultActions.REFRESH_RESULT}_SUCCESS`: {
      return state.set('translatedResult', Immutable.fromJS({
        empty: false,
        error: false,
        loading: false,
        chartData: action.payload.chartData,
        lastUpdated: action.payload.lastUpdated,
        gap: action.payload.gap
      }))
    }

    case `${resultActions.REFRESH_RESULT}_ERROR`: {
      return state.set('translatedResult', Immutable.fromJS({
        empty: true,
        error: true,
        loading: false,
        lastUpdated: moment(),
        errorStatus: action.payload.errorStatus,
        errorText: action.payload.errorText
      }))
    }

    case resultActions.saveChartTitle: {
      return state.set('chartTitle', action.payload.chartTitle)
    }

    case resultActions.setChartType: {
      return state.set('chartType', action.payload.chartType)
    }

    default: {
      return state
    }
  }
}

