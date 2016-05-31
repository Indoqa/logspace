import {connect} from 'react-redux'
import TimeSeriesEdit from './time-series-edit.react'
import * as actions from '../../actions/time-series'
import {refreshResult} from '../../actions/result'
import {cleanPropertyName} from './time-series-item.react'

const mapStateToProps = (state) => ({
  timeSeries: state.timeSeries.get('timeSeries'),
  editedTimeSeries: state.timeSeries.get('editedTimeSeries')
})

const mapDispatchToProps = (dispatch) => ({
  saveTimeSeries: () => {
    dispatch(actions.saveTimeseries())
    dispatch(refreshResult())
  },
  deleteTimeSeries: (id) => {
    dispatch(actions.deleteTimeseries(id))
  },
  changeTimeseriesProperty: (key, value) => {
    dispatch(actions.changeTimeseriesProperty(key, value))
  },
  cleanPropertyName: (name) => {
    cleanPropertyName(name)
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeSeriesEdit)
