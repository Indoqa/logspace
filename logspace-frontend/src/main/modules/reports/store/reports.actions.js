// @flow

import type {Action} from '../types/ReportsActions'
import type {Reports} from '../types/Reports'


export const loadReports = (): Action => ({
  type: 'REPORTS#LOAD_REPORTS',
})

export const loadReportsError = (error: any): Action => ({
  type: 'REPORTS#LOAD_REPORTS_ERROR',
  error,
})

export const setReports = (payload: Reports): Action => ({
  type: 'REPORTS#SET_REPORTS',
  payload,
})
