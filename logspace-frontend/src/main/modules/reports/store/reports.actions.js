// @flow

import type {Action} from '../types/ReportsActions'
import type {Result} from '../types/Result'


export const loadReports = (): Action => ({
  type: 'REPORTS#LOAD_REPORTS',
})

export const loadReportsError = (error: any): Action => ({
  type: 'REPORTS#LOAD_REPORTS_ERROR',
  error,
})

export const setResult = (payload: Result): Action => ({
  type: 'REPORTS#SET_RESULT',
  payload,
})
