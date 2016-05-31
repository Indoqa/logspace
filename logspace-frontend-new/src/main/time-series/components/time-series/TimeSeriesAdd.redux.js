import {connect} from 'react-redux'
import TimeSeriesAdd from './time-series-add.react'

import {showSuggestions} from '../../actions/suggestions'

const mapStateToProps = (state) => ({
  count: state.timeSeries.get('timeSeries').size
})

const mapDispatchToProps = (dispatch) => ({
  showSuggestions: () => {
    dispatch(showSuggestions())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeSeriesAdd)
