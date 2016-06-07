import {connect} from 'react-redux'
import TimeWindow from './TimeWindow.react'

import {showTimeWindowForm, selectPredefinedDate, selectDynamicDate, selectCustomDate} from '../../actions/timeWindow'
import {refreshResult} from '../../actions/result'

const mapStateToProps = (state) => ({
  timeWindow: state.timeWindow.get('selection'),
  dynamic: state.timeWindow.get('dynamic')
})

const mapDispatchToProps = (dispatch) => ({
  showTimeWindowForm: () => {
    dispatch(showTimeWindowForm())
  },
  selectPredefinedDate: (shortcut) => {
    dispatch(selectPredefinedDate(shortcut))
    dispatch(refreshResult())
  },
  selectDynamicDate: (duration, unit, gap) => {
    dispatch(selectDynamicDate(duration, unit, gap))
    dispatch(refreshResult())
  },
  selectCustomDate: (start, end, gap) => {
    dispatch(selectCustomDate(start, end, gap))
    dispatch(refreshResult())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeWindow)