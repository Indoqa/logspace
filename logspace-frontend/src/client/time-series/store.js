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
  scaleMin: 0,
  scaleMax: 100,
  space: ''
});

const colors = [
  '#e51c23',
  '#5677fc',
  '#ffeb3b',
  '#259b24',
  '#673ab7',  
  '#009688',
  '#e91e63',
  '#00bcd4',
  '#ff5722'
]

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
