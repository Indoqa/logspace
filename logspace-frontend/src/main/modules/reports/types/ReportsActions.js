// @flow

import type {Reports} from './Reports'

type LoadReportsAction = {
  type: 'REPORTS#LOAD_REPORTS',
}

type LoadReportsErrorAction = {
  type: 'REPORTS#LOAD_REPORTS_ERROR',
  error: any,
}

type SetReportsAction = {
  type: 'REPORTS#SET_REPORTS',
  payload: Reports,
}

export type Action =
  | SetReportsAction
  | LoadReportsAction
  | LoadReportsErrorAction
