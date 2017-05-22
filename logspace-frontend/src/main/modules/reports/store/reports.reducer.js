// @flow

import * as R from 'ramda'

import type {ReportsState} from '../types/ReportsState'
import type {Action} from '../types/ReportsActions'
import type {Paging} from '../../../commons/types/Paging'
import type {Sorting} from '../../../commons/types/Sorting'


const initialState = {
  paging: {
    start: 0,
    count: 10,
  },
  sorting: {
    property: 'name',
    ascending: true,
  },
  result: null,
  error: null,
  isLoading: false,
}

export default (state: ReportsState = initialState, action: Action) => {
  switch (action.type) {
    case 'REPORTS#LOAD_REPORTS':
      state = R.assoc('isLoading', true, state)
      state = R.assoc('result', null, state)
      state = R.assoc('error', null, state)
      return state

    case 'REPORTS#SET_REPORTS':
      state = R.assoc('isLoading', false, state)
      state = R.assoc('result', action.payload, state)
      return state

    case 'REPORTS#LOAD_REPORTS_ERROR':
      state = R.assoc('isLoading', false, state)
      state = R.assoc('error', action.error, state)
      return state

    default:
      return state
  }
}
