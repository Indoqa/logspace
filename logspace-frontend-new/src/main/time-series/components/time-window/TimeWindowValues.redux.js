import {connect} from 'react-redux'
import TimeWindowValues from './time-window-values.react'

import {showTimewindowForm, selectPredefinedDate} from '../../actions/time-window'

const mapStateToProps = (state) => ({
  timeWindow: state.timeWindow.get('selection')
})

const mapDispatchToProps = (dispatch) => ({
  showTimeWindowForm: () => {
    dispatch(showTimewindowForm())
  },
  selectPredefinedDate: (shortcut) => {
    dispatch(selectPredefinedDate(shortcut))
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeWindowValues)
