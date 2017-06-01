// @flow

import type {Result} from './Result'
import type {AutoReload} from '../../../commons/types/AutoReload'
import type {Paging} from '../../../commons/types/Paging'
import type {Sorting} from '../../../commons/types/Sorting'
import type {Interval} from '../../../commons/types/Interval'


export type AgentsState = {
  autoReload: AutoReload,
  paging: Paging,
  sorting: Sorting,
  interval: Interval,
  result: ?Result,
  error: ?string,
}
