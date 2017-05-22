
// @flow

import type {ReportsState} from './modules/reports/types/ReportsState'

type State = {
  reports:ReportsState,
}

export const selectReportsState = (state: State) => state.reports
