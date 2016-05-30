/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {fromJS} from 'immutable'

import {UPDATE_EDITABLE_STATE} from '../actions/editable'

const InitialState = fromJS({
  result: {
    chartTitle: {
      isEdited: false
    }
  }
})

export default (state = InitialState, action) => {
  switch (action.type) {
    case UPDATE_EDITABLE_STATE:
      return state.setIn([action.payload.id, action.payload.name], action.payload.state)
    default: {
      return state
    }
  }
}
