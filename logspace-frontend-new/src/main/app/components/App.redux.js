import {connect} from 'react-redux'

import App from './App.react'
import {onApplicationInitialized} from '../actions/history'

const mapStateToProps = () => ({
})

const mapDispatchToProps = (dispatch) => ({
  onApplicationInitialized: () => {
    dispatch(onApplicationInitialized())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(App)
