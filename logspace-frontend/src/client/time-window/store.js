/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import {Dispatcher} from 'flux'

import {register} from '../dispatcher'

import {timeWindowCursor} from '../state'

import * as actions from './actions'

export const TimeWindowStore_dispatchToken = register(({action, data}) => {

  switch (action) {
    case actions.onTimeWindowChange:
      timeWindowCursor(timeWindow => {
        return timeWindow.set("start", data.start)
      })
      timeWindowCursor(timeWindow => {
        return timeWindow.set("end", data.end)
      })
      timeWindowCursor(timeWindow => {
        return timeWindow.set("gap", data.gap)
      })
      break
  }
})
