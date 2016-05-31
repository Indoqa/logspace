import {connect} from 'react-redux'
import TimeSeriesList from './time-series-list.react'
import {editTimeseries} from '../../actions/time-series'
import {cleanPropertyName} from './time-series-item.react'

const mapStateToProps = (state) => ({
  items: state.timeSeries.get('timeSeries')
})

const mapDispatchToProps = (dispatch) => ({
  editTimeSeries: (timeSeries) => {
    dispatch(editTimeseries(timeSeries))
  },
  cleanPropertyName: (name) => {
    cleanPropertyName(name)
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeSeriesList)
