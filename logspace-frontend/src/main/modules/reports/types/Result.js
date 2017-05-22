// @flow

import type {Report} from './Report'

export type Result = {
  offset: number,
  totalCount: number,
  reports: Array<Report>
}
