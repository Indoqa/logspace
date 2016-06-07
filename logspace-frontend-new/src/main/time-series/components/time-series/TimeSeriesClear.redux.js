import {connect} from 'react-redux'
import TimeSeriesClear from './TimeSeriesClear.react'

import {resetTimeSeries} from '../../actions/timeSeries'
import {refreshResult} from '../../actions/result'

const mapStateToProps = (state) => ({
  count: state.timeSeries.get('timeSeries').size
})

const mapDispatchToProps = (dispatch) => ({
  resetTimeSeries: () => {
    dispatch(resetTimeSeries())
  },
  refreshResult: () => {
    dispatch(refreshResult())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeSeriesClear)
