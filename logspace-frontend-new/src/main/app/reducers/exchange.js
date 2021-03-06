/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {fromJS} from 'immutable'

import {CLEAR_EXPORT_BLOB, EXPORT_STATE, EXPORT_BLOB} from '../actions/exchange'

const InitialState = fromJS({
  serializedState: '',
  exportBlob: null
})

export default (state = InitialState, action) => {
  switch (action.type) {
    case CLEAR_EXPORT_BLOB:
      return state.set('exportBlob', null)
    case EXPORT_STATE:
      return state.set('serializedState', action.payload.serializedState)
    case EXPORT_BLOB:
      return state.set('exportBlob', action.payload.exportBlob)
    default: {
      return state
    }
  }
}
