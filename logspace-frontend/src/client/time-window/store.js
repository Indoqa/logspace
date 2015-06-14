/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import {Dispatcher} from 'flux'
import {register} from '../dispatcher'
import moment from 'moment'

import {timeWindowCursor} from '../state'
import * as actions from './actions'

import {TimeWindowSelection} from './constants'

export const TimeWindowStore_dispatchToken = register(({action, data}) => {

  switch (action) {
    case actions.selectPredefinedDate:
      timeWindowCursor(timeWindow => {
        return timeWindow.set('selection', data)
      })
      break

    case actions.selectCustomDate:
      const customSelection = new TimeWindowSelection({
        label: moment(data.start).format("MM-DD-YY, HH:mm") + " - \n" + moment(data.end).format("MM-DD-YY, HH:mm"),
        start: () => moment(data.start), 
        end: () => moment(data.end),
        gap: data.gap
      })

      timeWindowCursor(timeWindow => {
        return timeWindow.set('selection', customSelection)
      })
      break

    case actions.selectDynamicDate:
      const dynamicSelection = new TimeWindowSelection({
        label: 'last ' + data.duration + ' ' + data.unit,
        start: () => moment().subtract(data.duration, data.unit), 
        end: () => moment(),
        gap: data.gap
      })

      timeWindowCursor(timeWindow => {
        return timeWindow.set('selection', dynamicSelection)
      })
      break
  }
})
