// @flow

import {ajax} from 'rxjs/observable/dom/ajax'
import {Observable} from 'rxjs'
import 'rxjs/add/operator/debounce'

import {setResult, loadReportsError} from './reports.actions'
import {selectPaging, selectSorting} from './reports.selectors'

import type {Paging} from '../../../commons/types/Paging'
import type {Sorting} from '../../../commons/types/Sorting'
import type {ReportsState} from


const REPORTS_URL = (state: ReportsState) => {
  const paging = selectPaging(state)
  const sorting = selectSorting(state)

  return `/api/v1/reports?start=${paging.start}&count=${paging.count}&sort=${sorting.property} ${sorting.ascending ? 'asc' : 'desc'}`
}

const loadReportsEpic$ = (action$: any, store: any) => {
  return action$
    .ofType('REPORTS#LOAD_REPORTS')
    .switchMap(() => ajax.get(REPORTS_URL(store)))
    .map((data) => setResult(data.response))
    .catch((error) => Observable.of(loadReportsError(error)))
}

export default [loadReportsEpic$]
