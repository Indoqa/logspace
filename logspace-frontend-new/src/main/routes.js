/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import {IndexRoute, Route} from 'react-router'
import App from './app/components/App.redux'
import LogspacePage from './time-series/components/Page.redux'
import AgentActivityPage from './agent-activity/components/Page.redux'

export const ROUTE_AGENT_ACTIVITY = '/agent-activity'

const routes = (
  <Route component={App} path="/">
    <IndexRoute component={LogspacePage} />

    <Route component={AgentActivityPage} path={ROUTE_AGENT_ACTIVITY} />
  </Route>
)

export default routes
