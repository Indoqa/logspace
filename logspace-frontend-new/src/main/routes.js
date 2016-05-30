import React from 'react'
import {IndexRoute, Route} from 'react-router'
import App from './app/components/App.react'
import LogspacePage from './time-series/components/Page.redux'

const routes = (
  <Route component={App} path="/">
    <IndexRoute component={LogspacePage} />
  </Route>
)

export default routes
