/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import {Map, fromJS} from 'immutable'
import * as agentActivityActions from '../actions/agentActivity'

const InitialState = fromJS({
  loading: false,
  agentActivities: new Map,
  autoPlay: fromJS({
    running: false,
    countdown: 15
  }),
  duration: 300,
  maxHistoryValue: 0,
  sort: 'index asc'
})

export default (state = InitialState, action) => {
  switch (action.type) {
    case agentActivityActions.SET_DURATION: {
      return state.set('duration', action.payload)
    }

    case agentActivityActions.SET_SORT: {
      return state.set('sort', action.payload)
    }

    case agentActivityActions.START_AUTOPLAY: {
      return state
        .setIn(['autoPlay', 'running'], true)
        .setIn(['autoPlay', 'countdown'], 15)
    }

    case agentActivityActions.STOP_AUTOPLAY: {
      return state.setIn(['autoPlay', 'running'], false)
    }

    case agentActivityActions.RESET_AUTOPLAY_COUNTDOWN: {
      return state.setIn(['autoPlay', 'countdown'], 15)
    }

    case agentActivityActions.DECREMENT_AUTOPLAY_COUNTDOWN: {
      const currentValue = state.getIn(['autoPlay', 'countdown'])
      return state.setIn(['autoPlay', 'countdown'], currentValue - 1)
    }

    case `${agentActivityActions.LOAD_AGENT_ACTIVITIES}_START`: {
      return state.set('loading', true)
    }

    case `${agentActivityActions.LOAD_AGENT_ACTIVITIES}_SUCCESS`: {
      return state
        .set('agentActivities', fromJS(action.payload.agentActivities))
        .set('maxHistoryValue', action.payload.maxHistoryValue)
        .set('loading', false)
    }

    case `${agentActivityActions.LOAD_AGENT_ACTIVITIES}_ERROR`: {
      return state
      .set('agentActivities', new Map)
      .set('maxHistoryValue', 0)
      .set('loading', false)
    }

    default: {
      return state
    }
  }
}
