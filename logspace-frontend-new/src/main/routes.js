/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import {IndexRoute, Route} from 'react-router'
import App from './app/components/App.react'
import OverviewPage from './overview/components/Page.react'
import DashboardsPage from './dashboards/components/Page.react'
import AgentsPage from './agents/components/Page.redux'
import ReportPage from './time-series/components/Page.redux'
import ReportsPage from './reports/components/Page.redux'
import AlertsPage from './alerts/components/Page.react'
import UsersPage from './users/components/Page.react'

export const ROUTE_OVERVIEW = '/'
export const ROUTE_AGENTS = '/agents'
export const ROUTE_REPORTS = '/reports'
export const ROUTE_REPORT = '/reports/:reportId'
export const ROUTE_DASHBOARDS = '/dashboards'
export const ROUTE_ALERTS = '/alerts'
export const ROUTE_USERS = '/users'

const routes = (
  <Route component={App} path={ROUTE_OVERVIEW}>
    <IndexRoute component={OverviewPage} />

    <Route component={DashboardsPage} path={ROUTE_DASHBOARDS} />
    <Route component={AgentsPage} path={ROUTE_AGENTS} />
    <Route component={ReportPage} path={ROUTE_REPORT} />
    <Route component={ReportsPage} path={ROUTE_REPORTS} />
    <Route component={AlertsPage} path={ROUTE_ALERTS} />
    <Route component={UsersPage} path={ROUTE_USERS} />
  </Route>
)

export default routes
