import {connect} from 'react-redux'
import Result from './result.react'

import {updateEditableState} from '../../../app/actions/editable'

const mapStateToProps = (state) => ({
  autoPlay: state.result.get('autoPlay'),
  autoPlaySchedule: state.result.get('autoPlaySchedule'),
  chartType: state.result.get('chartType'),
  chartTitle: state.result.get('chartTitle'),
  chartTitleEditable: state.editable.get('result'),
  result: state.result.get('translatedResult'),
  timeSeries: state.timeSeries.get('timeSeries')
})

const mapDispatchToProps = (dispatch) => ({
  updateEditableState: (id, name, state) => {
    dispatch(updateEditableState(id, name, state))
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Result)
