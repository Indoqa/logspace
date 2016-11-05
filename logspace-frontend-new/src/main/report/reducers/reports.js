/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {Record, List, fromJS} from 'immutable'

import * as reportsActions from '../actions/reports'

const InitialState = Record({
  loading: false,
  reports: null,
  totalCount: 0,
  start: 0,
  count: 10
})

export default (state = new InitialState, action) => {
  switch (action.type) {
    case `${reportsActions.LOAD_REPORTS}_START`: {
      return state
        .set('loading', true)
        .set('reports', null)
        .set('totalCount', 0)
    }

    case `${reportsActions.LOAD_REPORTS}_SUCCESS`: {
      return state
        .set('loading', false)
        .set('reports', fromJS(action.payload.reports))
        .set('totalCount', action.payload.totalCount)
    }

    case `${reportsActions.LOAD_REPORTS}_ERROR`: {
      return state
        .set('loading', false)
    }

    default: {
      return state
    }
  }
}
