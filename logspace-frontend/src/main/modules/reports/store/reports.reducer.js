// @flow

import * as R from 'ramda'

import type {ReportsState} from '../types/ReportsState'
import type {Action} from '../types/ReportsActions'

const initialState = {
  reports: null,
  error: null,
  isLoading: false,
}

export default (state: ReportsState = initialState, action: Action) => {
  switch (action.type) {
    case 'REPORTS#LOAD_REPORTS':
      return R.assoc('isLoading', true, state)

    case 'REPORTS#SET_REPORTS':
      return R.assoc('reports', action.payload, state)

    case 'REPORTS#LOAD_REPORTS_ERROR':
      return R.assoc('error', action.error, state)

    default:
      return state
  }
}
