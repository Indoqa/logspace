import {connect} from 'react-redux'
import {fetchTime, clearTime} from '../actions/time'
import Buttons from './Buttons.react'

const mapStateToProps = () => ({
  //
})

const mapDispatchToProps = (dispatch) => ({
  loadVienna: () => {
    dispatch(fetchTime(10, 47))
  },
  loadNewYork: () => {
    dispatch(fetchTime(-74.0059700, 40.7142700))
  },
  loadInvalidLocation: () => {
    dispatch(fetchTime(-1000, -1000))
  },
  clear: () => {
    dispatch(clearTime())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Buttons)
