/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
 import * as actions from './actions';
import {timeSeriesCursor} from '../state';
import {register} from '../dispatcher';
import {Record} from 'immutable';
import {getRandomString} from '../../lib/getrandomstring';

const TimeSeriesItem = Record({
  id: '',
  color: '',
  agentId: '',
  propertyId: '',
  aggregate: '',
  space: ''
});

const colors = ['rgb(213, 191, 255)', 'rgb(191, 255, 218)', 'rgb(255, 209, 191)', 'rgb(247, 191, 255)', 'rgb(194, 191, 255)',
  'rgb(213, 191, 255)', 'rgb(191, 255, 218)', 'rgb(255, 209, 191)', 'rgb(247, 191, 255)', 'rgb(213, 191, 255)', 'rgb(191, 255, 218)',
  'rgb(255, 209, 191)', 'rgb(247, 191, 255)', 'rgb(213, 191, 255)', 'rgb(191, 255, 218)', 'rgb(255, 209, 191)',
  'rgb(247, 191, 255)'
];
  
export function getTimeSeries() {
  return timeSeriesCursor()
} 
  
export const TimeSeriesStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case actions.onTimeSeriesAdded:
      timeSeriesCursor(timeSeries => {
        const item = new TimeSeriesItem({
          id: getRandomString(),
          color: colors[timeSeries.size],
          agentId: data.agentId,
          propertyId: data.propertyId,
          aggregate: data.aggregate,
          space: 'development'
        }).toMap();
        return timeSeries.push(item);
      });
      break;
      
      case actions.onTimeSeriesDeleted:
        timeSeriesCursor(timeSeries => {
          return timeSeries.delete(timeSeries.indexOf(data))
        });
      break;
  }
});    
  
