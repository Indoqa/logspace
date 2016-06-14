import {connect} from 'react-redux'
import Result from './Result.react'

import * as actions from '../../actions/result'
import {updateEditableState} from '../../../app/actions/editable'

const mapStateToProps = (state) => ({
  autoPlay: state.result.get('autoPlay'),
  chartType: state.result.get('chartType'),
  chartTitle: state.result.get('chartTitle'),
  chartTitleEditable: state.editable.get('result'),
  result: state.result.get('translatedResult'),
  timeSeries: state.timeSeries.get('timeSeries')
})

const mapDispatchToProps = (dispatch) => ({
  updateEditableState: (id, name, state) => {
    dispatch(updateEditableState(id, name, state))
  },
  saveChartTitle: (title) => {
    dispatch(actions.saveChartTitle(title))
  },
  setChartType: (type) => {
    dispatch(actions.setChartType(type))
  },
  refreshResult: () => {
    dispatch(actions.refreshResult())
  },
  toggleAutoPlay: () => {
    dispatch(actions.toggleAutoPlay())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Result)
