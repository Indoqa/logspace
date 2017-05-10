// @flow

import React from 'react'
import {IndexRoute, Route} from 'react-router'
import Module1Page from './modules/module1/components/Module1Page.react'
import Module2Page from './modules/module2/components/Module2Page.react'

const routes = (
  <Route path="/">
    <IndexRoute component={Module1Page} />
    <Route component={Module2Page} path="/module2" />
  </Route>
)

export default routes
