// @flow

import type {AgentActivity} from './AgentActivity'


export type Result = {
  offset: number,
  totalCount: number,
  maxHistoryValue: number,
  agentActivities: AgentActivity[]
}
