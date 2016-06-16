/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import moment from 'moment'
import Immutable, {Record, fromJS} from 'immutable'

import * as actions from '../actions/timeWindow'
import * as exchangeActions from '../../app/actions/exchange'

import {TimeWindowSelection, selections, units} from '../actions/timeWindow.constants'

const InitialState = Record({
  selection: selections[0],
  dynamic: fromJS({
    range: {
      amount: 60,
      unit: units.get('minute')
    },
    gap: {
      amount: 1,
      unit: units.get('minute')
    }
  }),
  activeTab: 1
})

export default (state = new InitialState, action) => {
  switch (action.type) {
    case actions.SELECT_PREDEFINED_DATE: {
      return state.set('selection', fromJS(action.payload.selection))
    }

    case actions.SELECT_CUSTOM_DATE: {
      const customLabel =
      `${moment(action.payload.start).format('YY-MM-DD')}
      <span class="small"> ${moment(action.payload.start).format('HH:mm')}</span>
      -
      ${moment(action.payload.end).format('YY-MM-DD')}
      <span class="small"> ${moment(action.payload.end).format('HH:mm')}</span>`

      const customSelection = new TimeWindowSelection({
        label: customLabel,
        type: 'custom',
        start: () => moment(action.payload.start),
        end: () => moment(action.payload.end),
        gap: Immutable.fromJS(action.payload.gap)
      })

      return state.set('selection', customSelection)
    }

    case actions.SELECT_DYNAMIC_DATE: {
      const dynamicSelection = new TimeWindowSelection({
        label: `Last ${action.payload.duration} ${action.payload.unit.label.toLowerCase()}`,
        type: 'dynamic',
        start: () => moment().subtract(action.payload.duration, action.payload.unit.label),
        end: () => moment(),
        gap: Immutable.fromJS(action.payload.gap)
      })

      state = state.set('selection', dynamicSelection)
      state = state.set('dynamic', Immutable.fromJS({
        range: {
          amount: action.payload.duration,
          unit: action.payload.unit
        },
        gap: action.payload.gap
      }))

      return state
    }

    case actions.SHOW_TIMEWINDOW_TAB: {
      return state.set('activeTab', action.payload.index)
    }

    case actions.RESET_TIMEWINDOW: {
      return state.set('selection', selections[0])
    }

    case exchangeActions.RESET_STATE: {
      return state.merge(new InitialState)
    }

    case exchangeActions.IMPORT_STATE: {
      return state.merge(action.payload.importedState.timeWindow)
    }

    default: {
      return state
    }
  }
}
