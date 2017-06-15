// @flow

import type {Result} from './Result'
import type {Paging} from '../../../commons/types/Paging'
import type {Sorting} from '../../../commons/types/Sorting'


export type ReportsState = {
  paging: Paging,
  sorting: Sorting,
  result: ?Result,
  error: ?string,
  isLoading: boolean,
  deletedReportId: ?string,
}
