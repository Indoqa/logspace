import {connect} from 'react-redux'
import TimeWindowValues from './TimeWindowValues.react'

import {showTimeWindowForm, selectPredefinedDate} from '../../actions/timeWindow'

const mapStateToProps = (state) => ({
  timeWindow: state.timeWindow.get('selection')
})

const mapDispatchToProps = (dispatch) => ({
  showTimeWindowForm: () => {
    dispatch(showTimeWindowForm())
  },
  selectPredefinedDate: (shortcut) => {
    dispatch(selectPredefinedDate(shortcut))
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeWindowValues)
