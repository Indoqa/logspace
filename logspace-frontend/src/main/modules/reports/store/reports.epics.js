// @flow

import {ajax} from 'rxjs/observable/dom/ajax'
import {Observable} from 'rxjs'
import 'rxjs/add/operator/debounce'

import {loadReports, loadReportsError, setResult, deleteReportError, setDeletedReportId, setPage} from './reports.actions'
import {selectPaging, selectSorting, selectDeletedReportId} from './reports.selectors'

import type {Paging} from '../../../commons/types/Paging'
import type {Sorting} from '../../../commons/types/Sorting'


const REPORT_URL = (id: string) => `/api/reports/${id}`

const REPORTS_URL = (state: any) => {
  const paging : Paging = selectPaging(state)
  const sorting : Sorting = selectSorting(state)

  return `/api/reports?start=${paging.start}&count=${paging.count}&sort=${sorting.property} ${sorting.ascending ? 'asc' : 'desc'}`
}

const RESTORE_REPORT_URL = (state: any) => {
  const id = selectDeletedReportId(state)

  return `/api/reports/${id}?restore`
}

const loadReportsEpic$ = (action$: any, store: any) => {
  return action$
    .ofType('REPORTS#LOAD_REPORTS')
    .switchMap(() => ajax.get(REPORTS_URL(store.getState())))
    .map((data) => setResult(data.response))
    .catch((error) => Observable.of(loadReportsError(error)))
}

const deleteReportEpic$ = (action$: any) => {
  return action$
    .ofType('REPORTS#DELETE_REPORT')
    .switchMap((action) => ajax.delete(REPORT_URL(action.id)))
    .map((data) => setDeletedReportId(data.xhr.getResponseHeader('report-id')))
    .catch((error) => Observable.of(deleteReportError(error)))
}

const restoreDeletedReportEpic$ = (action$: any, store: any) => {
  return action$
    .ofType('REPORTS#RESTORE_DELETED_REPORT')
    .switchMap((id) => ajax.post(RESTORE_REPORT_URL(store.getState())))
    .map(() => setDeletedReportId(null))
    .catch((error) => Observable.of(deleteReportError(error)))
}

const refreshReportAfterDeletedReportIdChangedEpic$ = (action$: any) => {
  return action$
    .ofType('REPORTS#SET_DELETED_REPORT_ID')
    .map(loadReports)
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

export default [
  loadReportsEpic$,
  deleteReportEpic$,
  restoreDeletedReportEpic$,
  refreshReportAfterDeletedReportIdChangedEpic$,
  refreshReportAfterSortingChangedEpic$,
  refreshReportAfterPagingChangedEpic$
]
