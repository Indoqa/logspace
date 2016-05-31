import {connect} from 'react-redux'
import Options from './options.react'

import {refreshResult} from '../../actions/result'

const mapStateToProps = (state) => ({
  chartTitle: state.result.get('chartTitle')
})

const mapDispatchToProps = (dispatch) => ({
  getExportState: () => {
    console.log('getExportState() not moved to new redux yet!')
  },
  importState: () => {
    console.log('importState() not moved to new redux yet!')
  },
  refreshResult: () => {
    dispatch(refreshResult())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Options)
