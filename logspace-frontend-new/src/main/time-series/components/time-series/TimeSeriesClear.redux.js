import {connect} from 'react-redux'
import TimeSeriesClear from './time-series-clear.react'

import {resetTimeseries} from '../../actions/time-series'
import {refreshResult} from '../../actions/result'

const mapStateToProps = (state) => ({
  count: state.timeSeries.get('timeSeries').size
})

const mapDispatchToProps = (dispatch) => ({
  resetTimeSeries: () => {
    dispatch(resetTimeseries())
  },
  refreshResult: () => {
    dispatch(refreshResult())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeSeriesClear)
