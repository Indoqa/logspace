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
 import MaterialThemeProvider from 'indoqa-rebass-theme'
 import routes from './routes'
 import theme from './theme'

 import 'indoqa-rebass-theme/dist/indoqa-rebass-theme.css'

 const reduxConfig = {
   reducerFilePath: './reducers',
   getReducers: () => require('./reducers').default
 }

 render(
   <MaterialThemeProvider customTheme={theme}>
     <IndoqaApplication reduxConfig={reduxConfig} routerConfig={{routes}} />
   </MaterialThemeProvider>,
   document.getElementById('app')
 )

//
//
// import React from 'react'
// import {render} from 'react-dom'
//
// import IndoqaApplication from 'indoqa-react-app'
// import routes from './routes'
//
// if (process.env.NODE_ENV !== 'production') {
//   window.Perf = require('react-addons-perf')
// }
//
// const reduxConfig = {
//   reducerFilePath: './reducers',
//   getReducers: () => require('./reducers').default
// }
//
//
//
// render(
//   <IndoqaApplication reduxConfig={reduxConfig} routerConfig={{routes}} />,
//   document.getElementById('app')
// )
