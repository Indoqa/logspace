// @flow

import type {Result} from './Result'

type LoadReportsAction = {
  type: 'REPORTS#LOAD_REPORTS',
}

type LoadReportsErrorAction = {
  type: 'REPORTS#LOAD_REPORTS_ERROR',
  error: any,
}

type SetResultAction = {
  type: 'REPORTS#SET_RESULT',
  payload: Result,
}

type DeleteReportAction = {
  type: 'REPORTS#DELETE_REPORT',
  id: string,
}

type DeleteReportErrorAction = {
  type: 'REPORTS#DELETE_REPORT_ERROR',
  error: any,
}

type SetDeletedReportIdAction = {
  type: 'REPORTS#SET_DELETED_REPORT_ID',
  id: ?string,
}

type RestoreDeletedReportAction = {
  type: 'REPORTS#RESTORE_DELETED_REPORT',
}

type ToggleSortAction = {
  type: 'REPORTS#TOGGLE_SORT',
  property: string,
}

type SetPageAction = {
  type: 'REPORTS#SET_PAGE',
  page: number,
}

export type Action =
  | LoadReportsAction
  | LoadReportsErrorAction
  | SetResultAction
  | DeleteReportAction
  | DeleteReportErrorAction
  | RestoreDeletedReportAction
  | SetDeletedReportIdAction
  | ToggleSortAction
  | SetPageAction
