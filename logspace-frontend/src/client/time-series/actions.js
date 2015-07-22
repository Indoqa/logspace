/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import {dispatch} from '../dispatcher';
import setToString from '../../lib/settostring';

export function onTimeSeriesPropertyChanged(key, value) {
  var data ={key: key, value: value}	
  dispatch(onTimeSeriesPropertyChanged, data);
}

export function onTimeSeriesSaved() {
  dispatch(onTimeSeriesSaved);
}

export function onNewTimeSeries(agentDescriptions) {
  dispatch(onNewTimeSeries, agentDescriptions);
}

export function onEditTimeSeries(timeSeries) {
  dispatch(onEditTimeSeries, timeSeries);
}

export function onTimeSeriesDeleted(id) {
  dispatch(onTimeSeriesDeleted, id);
}

export function rememberSelectedProperty(value) {
  dispatch(rememberSelectedProperty, value);
}

export function reset() {
  dispatch(reset);
}

setToString('timeSeries', {
  onTimeSeriesSaved, onNewTimeSeries, onTimeSeriesDeleted, onEditTimeSeries, onTimeSeriesPropertyChanged, rememberSelectedProperty, reset
});  