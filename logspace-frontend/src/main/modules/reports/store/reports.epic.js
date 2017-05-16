import {ajax} from 'rxjs/observable/dom/ajax'
import {Observable} from 'rxjs'
import 'rxjs/add/operator/debounce'

import {setReports, loadReportsError} from './reports.actions'

const REPORTS_URL = '/reports'

const loadReportsEpic$ = (action$) => {
  return action$
    .ofType('REPORTS#LOAD_REPORTS')
    .switchMap(() => ajax.get(REPORTS_URL))
    .map((data) => setReports(data.response))
    .catch((error) => Observable.of(loadReportsError(error)))
}

export default [loadReportsEpic$]
