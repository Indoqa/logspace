// @flow

import {ajax} from 'rxjs/observable/dom/ajax'
import {Observable} from 'rxjs'
import 'rxjs/add/operator/debounce'

import {setResult, loadReports, loadReportsError, setPage} from './reports.actions'
import {selectPaging, selectSorting} from './reports.selectors'

import type {Paging} from '../../../commons/types/Paging'
import type {Sorting} from '../../../commons/types/Sorting'


const REPORTS_URL = (state: any) => {
  const paging : Paging = selectPaging(state)
  const sorting : Sorting = selectSorting(state)

  return `/api/reports?start=${paging.start}&count=${paging.count}&sort=${sorting.property} ${sorting.ascending ? 'asc' : 'desc'}`
}

const loadReportsEpic$ = (action$: any, store: any) => {
  return action$
    .ofType('REPORTS#LOAD_REPORTS')
    .switchMap(() => ajax.get(REPORTS_URL(store.getState())))
    .map((data) => setResult(data.response))
    .catch((error) => Observable.of(loadReportsError(error)))
}

const refreshReportAfterSortingChangedEpic$ = (action$: any) => {
  return action$
    .ofType('REPORTS#TOGGLE_SORT')
    .map(() => setPage(0))
}

const refreshReportAfterPagingChangedEpic$ = (action$: any) => {
  return action$
    .ofType('REPORTS#SET_PAGE')
    .map(loadReports)
}

export default [loadReportsEpic$, refreshReportAfterSortingChangedEpic$, refreshReportAfterPagingChangedEpic$]
