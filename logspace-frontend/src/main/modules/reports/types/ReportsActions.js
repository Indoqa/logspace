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

export type Action =
  | SetResultAction
  | LoadReportsAction
  | LoadReportsErrorAction
