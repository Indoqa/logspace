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
  scaleMin: 0,
  scaleMax: 100,
  space: '',
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
        return timeSeries.delete(timeSeries.indexOf(data))
      });
      break;

    case actions.onNewTimeSeries:
      editedTimeSeriesCursor(editedTimeSeries => {
        const item = new TimeSeriesItem({
          id: null,
          name: data.name,
          agentId: data.globalId,
          propertyId: data.propertyDescriptions[0].id,
          space: data.space,
          propertyDescriptions: Immutable.fromJS(data.propertyDescriptions),
          aggregate: "sum",
          color: COLORS[timeSeriesCursor().size]
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
  }
});

function addItem(item) {
  item.id = getRandomString() 
  
  timeSeriesCursor(timeSeries => {
    return timeSeries.push(Immutable.fromJS(item))
  });
}

function updateItem(item) {
  timeSeriesCursor(timeSeries => {
    var itemToUpdate = timeSeries.find(function(obj){return obj.get('id') === item.id;});
    var index = timeSeries.indexOf(itemToUpdate);
    return timeSeries.set(index, Immutable.fromJS(item))
  });
}