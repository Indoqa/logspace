import 'babel-polyfill'
import React from 'react'
import {render} from 'react-dom'
import {Provider} from 'react-redux'
import {Router, browserHistory} from 'react-router'
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
      <Router history={browserHistory}>
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
