/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import Immutable from 'immutable'
import {Record} from 'immutable'

import {register} from '../dispatcher'
import {getRandomString} from '../../lib/getrandomstring'

import {COLORS} from './constants'

import {timeSeriesCursor} from '../state'
import {timeSeriesDefaultsCursor} from '../state'
import {editedTimeSeriesCursor} from '../state'

import * as actions from './actions'

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
  axis: '',
  propertyDescriptions: []
})

export const TimeSeriesStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case actions.onTimeSeriesSaved:
      var item = editedTimeSeriesCursor().get('newItem').toJS()

      if (item.id == null) {
        addItem(item)
      } else {
        updateItem(item)
      }

      break

    case actions.onTimeSeriesDeleted:
      timeSeriesCursor(timeSeries => {
        var itemToDelete = timeSeries.find(function(obj){ return obj.get('id') === data })
        var index = timeSeries.indexOf(itemToDelete)
        return timeSeries.delete(timeSeries.indexOf(itemToDelete))
      })
      break

    case actions.onNewTimeSeries:
      var nextColor = getNextColor()
      var defaultProperty = getDefaultProperty(data.propertyDescriptions)
      var defaultAggregation = getDefaultAggregation(data.propertyDescriptions)

      editedTimeSeriesCursor(editedTimeSeries => {
        const item = new TimeSeriesItem({
          id: null,
          name: data.name,
          agentId: data.globalId,
          propertyId: defaultProperty,
          space: data.space,
          system: data.system,
          propertyDescriptions: Immutable.fromJS(data.propertyDescriptions),
          aggregate: defaultAggregation,
          color: nextColor
        }).toMap()

        return editedTimeSeries.set('newItem',  item)
      })
      break

    case actions.onEditTimeSeries:
      editedTimeSeriesCursor(editedTimeSeries => {
        return editedTimeSeries.set('newItem',  data)
      })
    break

    case actions.onTimeSeriesPropertyChanged:
      editedTimeSeriesCursor(editedTimeSeries => {
        return editedTimeSeries.setIn(['newItem', data.key],  data.value)
      })

      if (data.key === 'aggregate') {
        console.log('update!')
        timeSeriesDefaultsCursor(defaults => {
          return defaults.set('aggregate',  data.value)
        })  
      }
    break

    case actions.onAxisChanged:
      timeSeriesCursor(timeSeries => {
        const itemToUpdate = timeSeries
          .find(function(obj){ return obj.get('id') === data.id })
          .set('axis', data.axis)
          
        const index = timeSeries.indexOf(itemToUpdate)

        return timeSeries.update(index, item => itemToUpdate)
      })
    break
  }
})

function getNextColor() {
  const usedColors = timeSeriesCursor().map((item) => item.get('color'))
  const allColors = COLORS.slice()

  const freeColors = allColors.filter(function(item) {
    return usedColors.indexOf(item) === -1
  })

  return freeColors[0]
}

function getDefaultProperty(propertyDescriptions) {
  const suggestions = timeSeriesDefaultsCursor().get('propertyChain')

  return propertyDescriptions[0].id
}

function getDefaultAggregation(propertyDescriptions) {
   console.log(timeSeriesDefaultsCursor().get('aggregate'))
  return timeSeriesDefaultsCursor().get('aggregate')
}

function addItem(item) {
  item.id = getRandomString()

  timeSeriesCursor(timeSeries => {
    return timeSeries.push(Immutable.fromJS(item))
  })
}

function updateItem(item) {
  timeSeriesCursor(timeSeries => {
    var itemToUpdate = timeSeries.find(function(obj){ return obj.get('id') === item.id })
    var index = timeSeries.indexOf(itemToUpdate)
    return timeSeries.set(index, Immutable.fromJS(item))
  })
}
