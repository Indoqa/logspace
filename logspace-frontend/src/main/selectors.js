
// @flow

import type {AgentsState} from './modules/agents/types/AgentsState'
import type {ReportsState} from './modules/reports/types/ReportsState'

type State = {
  agents: AgentsState,
  reports: ReportsState,
}

export const selectAgentsState = (state: State) => state.agents
export const selectReportsState = (state: State) => state.reports
