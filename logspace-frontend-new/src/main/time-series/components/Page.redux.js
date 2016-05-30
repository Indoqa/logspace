import {connect} from 'react-redux'
import Page from './Page.react'

import {refreshResult} from '../actions/result'

const mapStateToProps = (state) => ({
  navDrawerCss: state.drawer.get('navDrawerCss'),
  mainCss: state.drawer.get('mainCss')
})

const mapDispatchToProps = (dispatch) => ({
  initialize: () => {
    console.log('TODO: implement initialize()')
    dispatch(refreshResult())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Page)
