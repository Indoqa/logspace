/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import {Map, fromJS} from 'immutable'
import * as agentsActions from '../actions/agents'

const INITIAL_COUNTDOWN = 15

const InitialState = fromJS({
  loading: false,
  agentActivities: new Map,
  autoPlay: fromJS({
    running: false,
    countdown: 0
  }),
  duration: 300,
  maxHistoryValue: 0,
  sort: 'index asc'
})

export default (state = InitialState, action) => {
  switch (action.type) {
    case agentsActions.SET_DURATION: {
      return state.set('duration', action.payload)
    }

    case agentsActions.SET_SORT: {
      return state.set('sort', action.payload)
    }

    case agentsActions.START_AUTOPLAY: {
      return state
        .setIn(['autoPlay', 'running'], true)
        .setIn(['autoPlay', 'countdown'], INITIAL_COUNTDOWN)
    }

    case agentsActions.STOP_AUTOPLAY: {
      return state
        .setIn(['autoPlay', 'running'], false)
        .setIn(['autoPlay', 'countdown'], 0)
    }

    case agentsActions.RESET_AUTOPLAY_COUNTDOWN: {
      return state.setIn(['autoPlay', 'countdown'], INITIAL_COUNTDOWN)
    }

    case agentsActions.DECREMENT_AUTOPLAY_COUNTDOWN: {
      const currentValue = state.getIn(['autoPlay', 'countdown'])
      return state.setIn(['autoPlay', 'countdown'], currentValue - 1)
    }

    case `${agentsActions.LOAD_AGENT_ACTIVITIES}_START`: {
      return state.set('loading', true)
    }

    case `${agentsActions.LOAD_AGENT_ACTIVITIES}_SUCCESS`: {
      return state
        .set('agentActivities', fromJS(action.payload.agentActivities))
        .set('maxHistoryValue', action.payload.maxHistoryValue)
        .set('loading', false)
    }

    case `${agentsActions.LOAD_AGENT_ACTIVITIES}_ERROR`: {
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
