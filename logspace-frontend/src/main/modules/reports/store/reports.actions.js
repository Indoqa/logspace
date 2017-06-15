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

export const deleteReport = (id: string): Action => ({
  type: 'REPORTS#DELETE_REPORT',
  id,
})

export const deleteReportError = (error: any): Action => ({
  type: 'REPORTS#DELETE_REPORT_ERROR',
  error,
})

export const setDeletedReportId = (id: ?string): Action => ({
  type: 'REPORTS#SET_DELETED_REPORT_ID',
  id,
})

export const restoreDeletedReport = (): Action => ({
  type: 'REPORTS#RESTORE_DELETED_REPORT',
})

export const toggleSort = (property: string): Action => ({
  type: 'REPORTS#TOGGLE_SORT',
  property,
})

export const setPage = (page: number): Action => ({
  type: 'REPORTS#SET_PAGE',
  page,
})
