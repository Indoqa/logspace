import {connect} from 'react-redux'
import Options from './Options.react'

import {refreshResult} from '../../actions/result'
import {exportState, importState} from '../../../app/actions/exchange'

const mapStateToProps = (state) => ({
  chartTitle: state.result.get('chartTitle'),
  exportedState: state.exchange.get('serializedState')
})

const mapDispatchToProps = (dispatch) => ({
  getExportState: () => {
    dispatch(exportState())
  },
  importState: (serializedState) => {
    dispatch(importState(serializedState))
  },
  refreshResult: () => {
    dispatch(refreshResult())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Options)
