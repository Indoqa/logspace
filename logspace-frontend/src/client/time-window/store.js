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
import Immutable from 'immutable'

import {timeWindowCursor} from '../state'
import {viewCursor} from '../state'
import * as actions from './actions'

import {TimeWindowSelection, selections} from './constants'

export const TimeWindowStore_dispatchToken = register(({action, data}) => {

  switch (action) {
    case actions.selectPredefinedDate:
      timeWindowCursor(timeWindow => {
        return timeWindow.set('selection', data)
      })
      break

    case actions.selectCustomDate:
      const customLabel = moment(data.start).format("YY-MM-DD") 
        + "<span class='small'> " + moment(data.start).format("HH:mm") + "</span>"
        + " - " 
        + moment(data.end).format("YY-MM-DD")
        + "<span class='small'> " + moment(data.end).format("HH:mm") + "</span>"

      const customSelection = new TimeWindowSelection({
        label: customLabel,
        type: 'custom',
        start: () => moment(data.start), 
        end: () => moment(data.end),
        gap: Immutable.fromJS(data.gap)
      })

      timeWindowCursor(timeWindow => {
        return timeWindow.set('selection', customSelection)
      })
      break

    case actions.selectDynamicDate:
      const dynamicSelection = new TimeWindowSelection({
        label: 'last ' + data.duration + ' ' + data.unit.label,
        type: 'dynamic',
        start: () => moment().subtract(data.duration, data.unit.label), 
        end: () => moment(),
        gap: Immutable.fromJS(data.gap)
      })

      timeWindowCursor(timeWindow => {
        return timeWindow.set('selection', dynamicSelection)
      })

      timeWindowCursor(timeWindow => {
        return timeWindow.set('dynamic', Immutable.fromJS({
          range: {
            amount: data.duration,
            unit: data.unit
          },
          gap: data.gap
        }))
      })

      break

    case actions.onTabOpen:
      viewCursor(view => {
        return view.set('activeTimeWindowTab', data)
      })
      break  

    case actions.reset:
      timeWindowCursor(timeWindow => {
        return timeWindow.set('selection', selections[0])
      })
      break
  }
})
