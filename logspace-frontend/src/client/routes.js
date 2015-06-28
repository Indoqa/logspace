/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import App from './app/app.react'
import TimeSeries from './pages/time-series.react'
import NotFound from './pages/notfound.react'
import React from 'react'
import {DefaultRoute, NotFoundRoute, Route} from 'react-router'

export default (
  <Route handler={App} path="/">
    <DefaultRoute handler={TimeSeries} name="home" />
    <NotFoundRoute handler={NotFound} name="not-found" />
  </Route>  
)
