/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import 'babel-polyfill'
import React from 'react'
import {render} from 'react-dom'
import {Provider} from 'react-redux'
import {Router, hashHistory} from 'react-router'
import store from './store'
import routes from './routes'

const enableHotReloading = () => {
  if (module.hot) {
    module.hot.accept()
  }
}

const renderAppWrappedInReduxAndRouter = () => {
  render(
    <Provider store={store}>
      <Router history={hashHistory}>
        {routes}
      </Router>
    </Provider>,
    document.getElementById('root')
  )
}

const main = () => {
  enableHotReloading()
  renderAppWrappedInReduxAndRouter()
}

// run main entry point
main()
