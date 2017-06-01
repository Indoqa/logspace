// @flow

import agentsEpics from './modules/agents/store/agents.epics'
import reportsEpics from './modules/reports/store/reports.epics'


export default [
  ...agentsEpics,
  ...reportsEpics,
]
