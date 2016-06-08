import {connect} from 'react-redux'
import TimeSeriesEdit from './TimeSeriesEdit.react'
import * as actions from '../../actions/timeSeries'
import {refreshResult} from '../../actions/result'
import {cleanPropertyName} from './TimeSeriesItem.react'

import {exportState} from '../../../app/actions/exchange'
import {saveStateChange} from '../../../app/actions/history'

const mapStateToProps = (state) => ({
  timeSeries: state.timeSeries.get('timeSeries'),
  editedTimeSeries: state.timeSeries.get('editedTimeSeries')
})

const mapDispatchToProps = (dispatch) => ({
  saveTimeSeries: () => {
    dispatch(actions.saveTimeSeries())
    // TODO FIXME
    dispatch(exportState())
    dispatch(saveStateChange())
    dispatch(refreshResult())
  },
  deleteTimeSeries: (id) => {
    dispatch(actions.deleteTimeSeries(id))
    dispatch(refreshResult())
  },
  changeTimeseriesProperty: (key, value) => {
    dispatch(actions.changeTimeSeriesProperty(key, value))
  },
  cleanPropertyName: (name) => {
    cleanPropertyName(name)
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeSeriesEdit)
