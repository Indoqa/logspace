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

import {COLORS, isSubitem, getReference} from './constants'

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
      deleteItem(data)
      break

    case actions.onNewTimeSeries:
      prepareNewItem(data)
      break

    case actions.onEditTimeSeries:
      editedTimeSeriesCursor(editedTimeSeries => {
        return editedTimeSeries.set('newItem',  data)
      })
      break

    case actions.onTimeSeriesPropertyChanged:
      changeProperty(data)
      break

    case actions.rememberSelectedProperty:
      rememberSelectedProperty(data)
      break    

    case actions.reset:
      reset()
      break      
  }
})

function addItem(item) {
  item.id = getRandomString()

  timeSeriesCursor(timeSeries => {
    return timeSeries.push(Immutable.fromJS(item))
  })
}

function updateItem(item) {
  timeSeriesCursor(timeSeries => {
    // map orphans to scale type 'auto'
    const updatedList = timeSeries.map((eachItem) => {
      if (isSubitem(item.scaleType) && getReference(eachItem.get('scaleType')) === item.id) {
        return eachItem.set('scaleType', item.scaleType)
      }

      return eachItem
    })

    var itemToUpdate = updatedList.find(function(obj){ return obj.get('id') === item.id })
    var index = updatedList.indexOf(itemToUpdate)
    return updatedList.set(index, Immutable.fromJS(item))
  })
}

function deleteItem(data) {
  timeSeriesCursor(timeSeries => {
    // map orphans to scale type 'auto'
    const updatedList = timeSeries.map((item) => {
      if (getReference(item.get('scaleType')) === data) {
        return item.set('scaleType', 'auto')
      }

      return item
    })

    var itemToDelete = updatedList.find(function(obj){ return obj.get('id') === data })
    var index = updatedList.indexOf(itemToDelete)
    return updatedList.delete(updatedList.indexOf(itemToDelete))
  })
}

function reset() {
  timeSeriesCursor(timeSeries => {
    return timeSeries.clear()
  })
}

function prepareNewItem(data) {
  var nextColor = getNextColor()
  var defaultProperty = getDefaultProperty(data.propertyDescriptions)
  var defaultAggregation = getDefaultAggregation(data.propertyDescriptions)
  var defaultScaleType = getDefaultScaleType()
  

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
      color: nextColor,
      scaleType: defaultScaleType
    }).toMap()

    return editedTimeSeries.set('newItem',  item)
  })
}

function changeProperty(data) {
  editedTimeSeriesCursor(editedTimeSeries => {
    return editedTimeSeries.setIn(['newItem', data.key],  data.value)
  })

  if (data.key === 'aggregate') {
    timeSeriesDefaultsCursor(defaults => {
      return defaults.set('aggregate',  data.value)
    })  
  }
  
  if (data.key === 'propertyId') {
    rememberSelectedProperty(data.value)
  }
}

function rememberSelectedProperty(value) {
  const stack = timeSeriesDefaultsCursor().get('propertyStack').toJS()
  stack.unshift(value)

  if (stack.length > 10) {
    stack.pop()
  }

  timeSeriesDefaultsCursor(defaults => {
   return defaults.set('propertyStack', Immutable.fromJS(stack))
  })  
}

function getNextColor() {
  const usedColors = timeSeriesCursor().map((item) => item.get('color'))
  const allColors = COLORS.slice()

  const freeColors = allColors.filter(function(item) {
    return usedColors.indexOf(item) === -1
  })

  return freeColors[0]
}

function getDefaultProperty(propertyDescriptions) {
  const suggestions = timeSeriesDefaultsCursor().get('propertyStack').toJS()

  for (var i = 0; i < suggestions.length; i++) {
    let suggestion = suggestions[i]
    if (containsDefault(propertyDescriptions, suggestion)) {
      return suggestion
    }
  }

  return propertyDescriptions[0].id
}

function containsDefault(propertyDescriptions, suggestion) {
  for (var i = 0; i < propertyDescriptions.length; i++) {
    let propertyDescription = propertyDescriptions[i]
    if (propertyDescription.id === suggestion) {
      return true
    }
  }

  return false  
}

function getDefaultAggregation(propertyDescriptions) {
  return timeSeriesDefaultsCursor().get('aggregate')
}

function getDefaultScaleType() {
  let lastMasterItem = null;
  timeSeriesCursor().forEach(function(item) {
    if (!isSubitem(item.get('scaleType'))) {
      lastMasterItem = item
    }   
  })

  if (lastMasterItem == null) {
    return 'auto'
  }

  return 'subitem-' + lastMasterItem.get('id')
}