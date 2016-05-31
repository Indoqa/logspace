import {connect} from 'react-redux'
import TimeWindow from './time-window.react'

import {showTimewindowForm, selectPredefinedDate, selectDynamicDate, selectCustomDate} from '../../actions/time-window'
import {refreshResult} from '../../actions/result'

const mapStateToProps = (state) => ({
  timeWindow: state.timeWindow.get('selection'),
  dynamic: state.timeWindow.get('dynamic')
})

const mapDispatchToProps = (dispatch) => ({
  showTimeWindowForm: () => {
    dispatch(showTimewindowForm())
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
