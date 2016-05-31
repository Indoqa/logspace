import {connect} from 'react-redux'
import Drawer from './Drawer.react'

import {closeDrawer} from '../../actions/drawer'

const mapStateToProps = (state) => ({
  activePanel: state.drawer.get('activePanel')
})

const mapDispatchToProps = (dispatch) => ({
  msg: (key) => key,
  close: () => {
    dispatch(closeDrawer())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Drawer)
