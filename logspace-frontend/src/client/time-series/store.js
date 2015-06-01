/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import Immutable from 'immutable';
import * as actions from './actions';
import {COLORS} from './constants';
import {timeSeriesCursor} from '../state';
import {editedTimeSeriesCursor} from '../state';
import {register} from '../dispatcher';
import {Record} from 'immutable';
import {getRandomString} from '../../lib/getrandomstring';

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
  type: 'line',
  space: '',
  system: '',
  axis: '',
  propertyDescriptions: []
});

export function getTimeSeries() {
  return timeSeriesCursor()
} 

export function getEditedTimeSeries() {
  return editedTimeSeriesCursor()
} 

export const TimeSeriesStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case actions.onTimeSeriesSaved:
      var item = getEditedTimeSeries().get("newItem").toJS();
      
      if (item.id == null) {
        addItem(item);
      } else {
        updateItem(item);
      }
      
      break;

    case actions.onTimeSeriesDeleted:
      timeSeriesCursor(timeSeries => {
        var itemToDelete = timeSeries.find(function(obj){ return obj.get('id') === data });
        var index = timeSeries.indexOf(itemToDelete);
        return timeSeries.delete(timeSeries.indexOf(itemToDelete))
      });
      break;

    case actions.onNewTimeSeries:
      var nextColor = getNextColor();
      var defaultProperty = getDefaultProperty(data.propertyDescriptions);

      editedTimeSeriesCursor(editedTimeSeries => {
        const item = new TimeSeriesItem({
          id: null,
          name: data.name,
          agentId: data.globalId,
          propertyId: defaultProperty,
          space: data.space,
          system: data.system,
          propertyDescriptions: Immutable.fromJS(data.propertyDescriptions),
          aggregate: "sum",
          color: nextColor
        }).toMap();
        
        return editedTimeSeries.set("newItem",  item)
      });
      break;

    case actions.onEditTimeSeries:
      editedTimeSeriesCursor(editedTimeSeries => {
        return editedTimeSeries.set("newItem",  data)
      });
    break;

    case actions.onTimeSeriesPropertyChanged:
      editedTimeSeriesCursor(editedTimeSeries => {
        return editedTimeSeries.setIn(["newItem", data.key],  data.value)
      });
    break;  

    case actions.onAxisChanged:
      timeSeriesCursor(timeSeries => {
        var itemToUpdate = timeSeries.find(function(obj){ return obj.get('id') === data.id });
        var index = timeSeries.indexOf(itemToUpdate);
        
        itemToUpdate = itemToUpdate.set("axis", data.axis)
        
        return timeSeries.update(index, item => itemToUpdate);
      });
    break;
  }
});

function getNextColor() {
  var usedColors = getTimeSeries().map(function(item) {
      return item.get("color")
  });

  var allColors = COLORS.slice();
  
  var freeColors = allColors.filter(function(item) {
    return usedColors.indexOf(item) === -1;
  });

  return freeColors[0]
}

function getDefaultProperty(propertyDescriptions) {
  for (var i = 0; i < propertyDescriptions.length; i++) {
    let propertyDescription = propertyDescriptions[0]
    if (propertyDescription.propertyType != "STRING") {
      return propertyDescription.id;
    }
  }

  return null;
}

function addItem(item) {
  item.id = getRandomString() 
  
  timeSeriesCursor(timeSeries => {
    return timeSeries.push(Immutable.fromJS(item))
  });
}

function updateItem(item) {
  timeSeriesCursor(timeSeries => {
    var itemToUpdate = timeSeries.find(function(obj){ return obj.get('id') === item.id });
    var index = timeSeries.indexOf(itemToUpdate);
    return timeSeries.set(index, Immutable.fromJS(item))
  });
}