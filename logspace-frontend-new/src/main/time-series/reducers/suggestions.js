/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import Immutable, {Record} from 'immutable'

import * as actions from '../actions/suggestions'

const InitialState = Record({
  request: {
    text: null,
    system: null,
    space: null,
    propertyId: null
  },
  result: {
    spaces: [],
    systems: [],
    propertyNames: [],
    agentDescriptions: [],
    loading: false
  }
})

export default (state = new InitialState, action) => {
  switch (action.type) {
    case actions.SET_SUGGESTION_QUERY:
      return state.setIn(['request', 'text'], Immutable.fromJS(action.payload.query))

    case actions.SELECT_SYSTEM:
      return state.setIn(['request', 'system'], Immutable.fromJS(action.payload.system))

    case actions.CLEAR_SYSTEM:
      return state.setIn(['request', 'system'], null)

    case actions.SELECT_SPACE:
      return state.setIn(['request', 'space'], Immutable.fromJS(action.payload.space))

    case actions.CLEAR_SPACE:
      return state.setIn(['request', 'space'], null)

    case actions.SELECT_PROPERTY:
      return state.setIn(['request', 'property'], Immutable.fromJS(action.payload.property))

    case actions.CLEAR_PROPERTY:
      return state.setIn(['request', 'property'], null)

    case `${actions.REFRESH_SUGGESTIONS}_START`:
      return state.set('result', Immutable.fromJS({
        error: false,
        loading: true,
        spaces: [],
        systems: [],
        propertyNames: [],
        agentDescriptions: []
      }))

    case `${actions.REFRESH_SUGGESTIONS}_SUCCESS`:
      return state.set('result', Immutable.fromJS({
        error: false,
        loading: false,
        spaces: action.payload.spaces,
        systems: action.payload.systems,
        propertyNames: action.payload.propertyNames,
        agentDescriptions: action.payload.agentDescriptions
      }))

    case `${actions.REFRESH_SUGGESTIONS}_ERROR`:
      return state.set('result', Immutable.fromJS({
        error: true,
        loading: false,
        spaces: [],
        systems: [],
        propertyNames: [],
        agentDescriptions: []
      }))

    default:
      return state
  }
}
