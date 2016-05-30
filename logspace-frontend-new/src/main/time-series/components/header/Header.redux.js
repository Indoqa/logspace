import {connect} from 'react-redux'
import Header from './header.react'

import {showOptions} from '../../actions/options'

const mapStateToProps = () => ({
  //
})

const mapDispatchToProps = (dispatch) => ({
  showOptions: () => {
    dispatch(showOptions())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Header)
