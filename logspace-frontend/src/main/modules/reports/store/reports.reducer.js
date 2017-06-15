// @flow

import * as R from 'ramda'

import type {ReportsState} from '../types/ReportsState'
import type {Action} from '../types/ReportsActions'


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
  deletedReportId: null,
}

export default (state: ReportsState = initialState, action: Action) => {
  switch (action.type) {
    case 'REPORTS#LOAD_REPORTS':
      state = R.assoc('isLoading', true, state)
      state = R.assoc('result', null, state)
      state = R.assoc('error', null, state)
      return state

    case 'REPORTS#LOAD_REPORTS_ERROR':
      state = R.assoc('isLoading', false, state)
      state = R.assoc('error', action.error, state)
      return state

    case 'REPORTS#SET_RESULT':
      state = R.assoc('isLoading', false, state)
      state = R.assoc('result', action.payload, state)
      return state

    case 'REPORTS#SET_DELETED_REPORT_ID':
      state = R.assoc('deletedReportId', action.id, state)
      return state

    case 'REPORTS#TOGGLE_SORT':
      if (state.sorting.property === action.property) {
        state = R.assocPath(['sorting', 'ascending'], !state.sorting.ascending, state)
      } else {
        state = R.assocPath(['sorting', 'property'], action.property, state)
        state = R.assocPath(['sorting', 'ascending'], true, state)
      }

      return state

    case 'REPORTS#SET_PAGE':
      state = R.assocPath(['paging', 'start'], action.page * state.paging.count, state)

      return state

    default:
      return state
  }
}
