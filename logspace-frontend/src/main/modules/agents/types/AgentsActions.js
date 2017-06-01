// @flow

import type {Result} from './Result'
import type {Sorting} from '../../../commons/types/Sorting'


type SetResultAction = {
  type: 'AGENTS#SET_RESULT',
  payload: Result,
}

type LoadActivitiesAction = {
  type: 'AGENTS#LOAD_ACTIVITIES',
}

type LoadActivitiesErrorAction = {
  type: 'AGENTS#LOAD_ACTIVITIES_ERROR',
  error: any,
}

type SetSortingAction = {
  type: 'AGENTS#SET_SORTING',
  sorting: Sorting,
}

type SetPageAction = {
  type: 'AGENTS#SET_PAGE',
  page: number,
}

type SetIntervalAction = {
  type: 'AGENTS#SET_INTERVAL',
  seconds: number,
}

type StartAutoReloadActiveAction = {
  type: 'AGENTS#START_AUTO_RELOAD',
}

type StopAutoReloadActiveAction = {
  type: 'AGENTS#STOP_AUTO_RELOAD',
}

type UpdateCountdownAction = {
  type: 'AGENTS#UPDATE_COUNTDOWN',
}

export type AgentsAction =
  | SetResultAction
  | LoadActivitiesAction
  | LoadActivitiesErrorAction
  | SetSortingAction
  | SetPageAction
  | SetIntervalAction
  | StartAutoReloadActiveAction
  | StopAutoReloadActiveAction
  | UpdateCountdownAction
