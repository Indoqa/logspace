// @flow

import React from 'react'
import {IndexRoute, Route} from 'react-router'
import Module1Page from './modules/module1/components/Module1Page.react'
import Module2Page from './modules/module2/components/Module2Page.react'

import AgentsPage from './modules/agents/components/AgentsPage.react'
import DashboardsPage from './modules/dashboards/components/DashboardsPage.react'
import ReportsPage from './modules/reports/components/ReportsPage.redux'
import AlertsPage from './modules/alerts/components/AlertsPage.react'
import UsersPage from './modules/users/components/UsersPage.react'


const routes = (
  <Route path="/">
    <IndexRoute component={Module1Page} />
    <Route component={Module2Page} path="/module2" />
    <Route component={AgentsPage} path="/agents" />
    <Route component={DashboardsPage} path="/dashboards" />
    <Route component={ReportsPage} path="/reports" />
    <Route component={AlertsPage} path="/alerts" />
    <Route component={UsersPage} path="/users" />
  </Route>
)

export default routes
