/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {fromJS} from 'immutable'

import {HASH_CHANGED} from '../actions/history'

const InitialState = fromJS({
  lastHash: ''
})

export default (state = InitialState, action) => {
  switch (action.type) {
    case HASH_CHANGED:
      return state.set('lastHash', action.payload.hash)
    default: {
      return state
    }
  }
}
