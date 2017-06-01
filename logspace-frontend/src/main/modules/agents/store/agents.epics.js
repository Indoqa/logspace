// @flow

import {ajax} from 'rxjs/observable/dom/ajax'
import {Observable} from 'rxjs'
import 'rxjs/add/operator/debounce'

import {setResult, loadActivities, loadActivitiesError, setPage, updateCountdown} from './agents.actions'
import {selectAutoReload, selectInterval, selectPaging, selectSorting} from './agents.selectors'

import {createStartAutoReloadEpic, createOnCountdownUpdatedEpic} from '../../../commons/store/autoReload.epics'

import type {Interval} from '../../../commons/types/Interval'
import type {Paging} from '../../../commons/types/Paging'
import type {Sorting} from '../../../commons/types/Sorting'


const ACTIVITIES_URL = (state: any) => {
  const interval: Interval = selectInterval(state)
  const paging : Paging = selectPaging(state)
  const sorting : Sorting = selectSorting(state)

  return '/api/agent-activity?'
    + `start=${paging.start}&count=${paging.count}`
    + `&duration=${interval.seconds}&steps=${interval.steps}`
    + `&sort=${sorting.property} ${sorting.ascending ? 'asc' : 'desc'}`
}

const loadActivitiesEpic$ = (action$: any, store: any) => {
  return action$
    .ofType('AGENTS#LOAD_ACTIVITIES')
    .switchMap(() => ajax.get(ACTIVITIES_URL(store.getState())))
    .map((data) => setResult(data.response))
    .catch((error) => Observable.of(loadActivitiesError(error)))
}

const refreshActivitiesAfterIntervalChangedEpic$ = (action$: any) => {
  return action$
    .ofType('AGENTS#SET_INTERVAL')
    .map(loadActivities)
}

const refreshActivitiesAfterSortingChangedEpic$ = (action$: any) => {
  return action$
    .ofType('AGENTS#SET_SORTING')
    .map(() => setPage(0))
}

const refreshActivitiesAfterPagingChangedEpic$ = (action$: any) => {
  return action$
    .ofType('AGENTS#SET_PAGE')
    .map(loadActivities)
}

const startAutoReloadActiveEpic$ = createStartAutoReloadEpic('AGENTS#START_AUTO_RELOAD', 'AGENTS#STOP_AUTO_RELOAD', updateCountdown)

const onCountdownUpdatedEpic$ = createOnCountdownUpdatedEpic('AGENTS#UPDATE_COUNTDOWN', selectAutoReload, loadActivities)

export default [
  loadActivitiesEpic$,
  refreshActivitiesAfterIntervalChangedEpic$,
  refreshActivitiesAfterSortingChangedEpic$,
  refreshActivitiesAfterPagingChangedEpic$,
  startAutoReloadActiveEpic$,
  onCountdownUpdatedEpic$]
