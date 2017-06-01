// @flow

import type {AgentsAction} from '../types/AgentsActions'
import type {Result} from '../types/Result'
import type {Sorting} from '../../../commons/types/Sorting'


export const loadActivities = (): AgentsAction => ({
  type: 'AGENTS#LOAD_ACTIVITIES',
})

export const loadActivitiesError = (error: any): AgentsAction => ({
  type: 'AGENTS#LOAD_ACTIVITIES_ERROR',
  error,
})

export const setResult = (payload: Result): AgentsAction => ({
  type: 'AGENTS#SET_RESULT',
  payload,
})

export const setSorting = (sorting: Sorting): AgentsAction => ({
  type: 'AGENTS#SET_SORTING',
  sorting,
})

export const setPage = (page: number): AgentsAction => ({
  type: 'AGENTS#SET_PAGE',
  page,
})

export const setInterval = (seconds: number): AgentsAction => ({
  type: 'AGENTS#SET_INTERVAL',
  seconds,
})

export const startAutoReload = (): AgentsAction => ({
  type: 'AGENTS#START_AUTO_RELOAD',
})

export const stopAutoReload = (): AgentsAction => ({
  type: 'AGENTS#STOP_AUTO_RELOAD',
})

export const updateCountdown = (): AgentsAction => ({
  type: 'AGENTS#UPDATE_COUNTDOWN',
})
