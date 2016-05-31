/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import Immutable from 'immutable'
import {Record, List, fromJS} from 'immutable'
import {getRandomString} from '../../app/utils/getRandomString'
import {COLORS, isSubitem, getReference} from '../actions/time-series.constants'
import * as actions from '../actions/time-series'

const TimeSeriesItem = Record({
  id: '',
  color: '',
  name: '',
  agentId: '',
  propertyId: '',
  aggregate: '',
  scaleType: 'auto',
  scaleMin: 0,
  scaleMax: 100,
  type: 'spline',
  space: '',
  system: '',
  propertyDescriptions: []
})

const InitialState = Record({
  timeSeries: new List,
  defaults: fromJS({
    propertyStack: [],
    aggregate: 'count'
  }),
  editedTimeSeries: null
})

function rememberSelectedProperty(state, value) {
  const stack = state.getIn(['defaults', 'propertyStack']).toJS()
  stack.unshift(value)

  if (stack.length > 10) {
    stack.pop()
  }

  return state.setIn(['defaults', 'propertyStack'], Immutable.fromJS(stack))
}

function getNextColor(state) {
  const usedColors = state.get('timeSeries').map((item) => item.get('color'))
  const allColors = COLORS.slice()

  const freeColors = allColors.filter(item => usedColors.indexOf(item) === -1)
  return freeColors[0]
}

function containsDefault(propertyDescriptions, suggestion) {
  for (let i = 0; i < propertyDescriptions.length; i++) {
    const propertyDescription = propertyDescriptions[i]
    if (propertyDescription.id === suggestion) {
      return true
    }
  }

  return false
}

function getDefaultProperty(state, propertyDescriptions) {
  const suggestions = state.getIn(['defaults', 'propertyStack']).toJS()

  for (let i = 0; i < suggestions.length; i++) {
    const suggestion = suggestions[i]
    if (containsDefault(propertyDescriptions, suggestion)) {
      return suggestion
    }
  }

  return propertyDescriptions[0].id
}

function getDefaultAggregation(state) {
  return state.getIn(['defaults', 'aggregate'])
}

function getDefaultScaleType(state) {
  let lastMasterItem = null

  state.get('timeSeries').forEach(item => {
    if (!isSubitem(item.get('scaleType'))) {
      lastMasterItem = item
    }
  })

  if (lastMasterItem === null) {
    return 'auto'
  }

  return `subitem-${lastMasterItem.get('id')}`
}

export default (state = new InitialState, action) => {
  switch (action.type) {
    case actions.SAVE_TIMESERIES: {
      const item = state.get('editedTimeSeries').toJS()

      if (item.id === null) {
        item.id = getRandomString()
        return state.update('timeSeries', (timeSeries) => timeSeries.push(Immutable.fromJS(item)))
      }

      return state.update('timeSeries', (timeSeries) => {
        // map orphans to scale type 'auto'
        const updatedList = timeSeries.map((eachItem) => {
          if (isSubitem(item.scaleType) && getReference(eachItem.get('scaleType')) === item.id) {
            return eachItem.set('scaleType', item.scaleType)
          }

          return eachItem
        })

        const itemToUpdate = updatedList.find(obj => obj.get('id') === item.id)
        const index = updatedList.indexOf(itemToUpdate)
        return updatedList.set(index, Immutable.fromJS(item))
      })
    }

    case actions.DELETE_TIMESERIES: {
      return state.update('timeSeries', (timeSeries) => {
        // map orphans to scale type 'auto'
        const updatedList = timeSeries.map((item) => {
          if (getReference(item.get('scaleType')) === action.payload.id) {
            return item.set('scaleType', 'auto')
          }

          return item
        })

        const itemToDelete = updatedList.find(obj => obj.get('id') === action.payload.id)
        return updatedList.delete(updatedList.indexOf(itemToDelete))
      })
    }

    case actions.ADD_TIMESERIES: {
      const data = action.payload.agentDescriptions
      const item = new TimeSeriesItem({
        id: null,
        name: data.name,
        agentId: data.globalId,
        propertyId: getDefaultProperty(state, data.propertyDescriptions),
        space: data.space,
        system: data.system,
        propertyDescriptions: Immutable.fromJS(data.propertyDescriptions),
        aggregate: getDefaultAggregation(state, data.propertyDescriptions),
        color: getNextColor(state),
        scaleType: getDefaultScaleType(state)
      })

      return state.set('editedTimeSeries', item)
    }

    case actions.EDIT_TIMESERIES: {
      return state.set('editedTimeSeries', action.payload.timeSeries)
    }

    case actions.CHANGE_TIMESERIES_PROPERTY: {
      state = state.setIn(['editedTimeSeries', action.payload.key], action.payload.value)

      if (action.payload.key === 'aggregate') {
        state.setIn(['defaults', 'aggregate'], action.payload.value)
      }

      if (action.payload.key === 'propertyId') {
        state = rememberSelectedProperty(state, action.payload.value)
      }

      return state
    }

    case actions.REMEMBER_SELECTED_TIMESERIES_PROPERTY: {
      return rememberSelectedProperty(state, action.payload.value)
    }

    case actions.RESET_TIMESERIES: {
      return state.update('timeSeries', timeSeries => timeSeries.clear())
    }

    default: {
      return state
    }
  }
}
