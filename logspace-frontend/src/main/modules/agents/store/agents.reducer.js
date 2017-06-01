// @flow

import * as R from 'ramda'

import * as AutoReload from '../../../commons/store/autoReload.reducer'

import type {AgentsState} from '../types/AgentsState'
import type {AgentsAction} from '../types/AgentsActions'


const initialState = {
  autoReload: {
    countdown: 0,
    active: false,
    isLoading: false,
  },
  paging: {
    start: 0,
    count: 10,
  },
  sorting: {
    property: 'count',
    ascending: false,
  },
  interval: {
    seconds: 900,
    steps: 50,
  },
  result: null,
  error: null,
}

const onStartLoading = AutoReload.createOnStartLoading('autoReload')
const onResult = AutoReload.createOnResult('autoReload')
const onError = AutoReload.createOnError('autoReload')
const startAutoReload = AutoReload.createStart('autoReload')
const stopAutoReload = AutoReload.createStop('autoReload')
const updateCountdown = AutoReload.createUpdateCountdown('autoReload')

export default (state: AgentsState = initialState, action: AgentsAction) => {
  switch (action.type) {
    case 'AGENTS#START_AUTO_RELOAD':
      return startAutoReload(state)

    case 'AGENTS#STOP_AUTO_RELOAD':
      return stopAutoReload(state)

    case 'AGENTS#UPDATE_COUNTDOWN':
      return updateCountdown(state)

    case 'AGENTS#LOAD_ACTIVITIES':
      state = R.assoc('result', null, state)
      state = R.assoc('error', null, state)
      state = onStartLoading(state)
      return state

    case 'AGENTS#LOAD_ACTIVITIES_ERROR':
      state = R.assoc('error', action.error, state)
      state = onError(state)
      return state

    case 'AGENTS#SET_RESULT':
      state = R.assoc('result', action.payload, state)
      state = onResult(state)
      return state

    case 'AGENTS#SET_SORTING':
      state = R.assoc('sorting', action.sorting, state)
      return state

    case 'AGENTS#SET_INTERVAL':
      state = R.assocPath(['interval', 'seconds'], action.seconds, state)
      return state

    case 'AGENTS#SET_PAGE':
      state = R.assocPath(['paging', 'start'], action.page * state.paging.count, state)
      return state

    default:
      return state
  }
}
