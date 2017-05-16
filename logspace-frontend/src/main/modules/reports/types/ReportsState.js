// @flow

import type {Reports} from './Reports'

export type ReportsState = {
  reports: ?Reports,
  error: ?string,
  isLoading: boolean
}
