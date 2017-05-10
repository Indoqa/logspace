// @flow

import React from 'react'
import {render} from 'react-dom'
import IndoqaApplication from 'indoqa-react-app'
import {IndoqaFela} from 'indoqa-react-fela'
import routes from './routes.react'
import theme from './theme'

const reduxConfig = {
  reducers: require('./reducers').default,
  epics: require('./epics').default,
}

const initFela = (renderer) => {
  renderer.renderStatic('html, body, #app {height: 100%}')
}

render(
  <IndoqaFela init={initFela} customTheme={theme}>
    <IndoqaApplication reduxConfig={reduxConfig} routerConfig={{routes}} />
  </IndoqaFela>,
  document.getElementById('app')
)
