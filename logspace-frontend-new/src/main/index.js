/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import {render} from 'react-dom'

import IndoqaApplication from 'indoqa-react-app'
import routes from './routes'

if (process.env.NODE_ENV !== 'production') {
  window.Perf = require('react-addons-perf')
}

const reduxConfig = {
  reducerFilePath: './reducers',
  getReducers: () => require('./reducers').default
}

render(
  <IndoqaApplication reduxConfig={reduxConfig} routerConfig={{routes}} />,
  document.getElementById('app')
)
