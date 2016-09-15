/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import {render} from 'react-dom'
import {Provider} from 'react-redux'
import {Router, browserHistory} from 'react-router'
import store from './store'
import routes from './routes'

if (process.env.NODE_ENV !== 'production') {
  window.Perf = require('react-addons-perf')
}

const enableHotReloading = () => {
  if (module.hot) {
    module.hot.accept()
  }
}

const renderAppWrappedInReduxAndRouter = () => {
  render(
    <Provider store={store}>
      <Router history={browserHistory}>
        {routes}
      </Router>
    </Provider>,
    document.getElementById('app')
  )
}

const main = () => {
  enableHotReloading()
  renderAppWrappedInReduxAndRouter()
}

main()
