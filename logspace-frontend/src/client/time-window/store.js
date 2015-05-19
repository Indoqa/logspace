/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import * as actions from './actions';
import {timeWindowCursor} from '../state';
import {register} from '../dispatcher';
import {Dispatcher} from 'flux';

export function getTimeWindow() {
  return timeWindowCursor();
}

export const TIMEWINDOW_CONSTANTS = {
  gap: {
    second: 1,
    minute: 60,
    hour: 3600,
    day: 86400,
    week: 604800,
    month: 2629740,
    year: 31556900
  }
}

export const TimeWindowStore_dispatchToken = register(({action, data}) => {

  switch (action) {
    case actions.onTimeWindowChange:
      timeWindowCursor(timeWindow => {
        return timeWindow.set("start", data.start);
      });
      timeWindowCursor(timeWindow => {
        return timeWindow.set("end", data.end);
      });
      timeWindowCursor(timeWindow => {
        return timeWindow.set("gap", data.gap);
      });
      break;
  }
});
