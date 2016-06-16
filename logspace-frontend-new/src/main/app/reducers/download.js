/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {fromJS} from 'immutable'

import {CLEAR_DOWNLOAD, REQUEST_DOWNLOAD} from '../actions/download'

const InitialState = fromJS({
  downloadBlob: null
})

export default (state = InitialState, action) => {
  switch (action.type) {
    case `${REQUEST_DOWNLOAD}_SUCCESS`:
      return state.set('downloadBlob', action.payload)
    case CLEAR_DOWNLOAD:
      return state.set('downloadBlob', null)
    default: {
      return state
    }
  }
}
