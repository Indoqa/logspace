import {connect} from 'react-redux'
import TimeSeriesList from './TimeSeriesList.react'
import {editTimeSeries} from '../../actions/timeSeries'
import {cleanPropertyName} from './TimeSeriesItem.react'

const mapStateToProps = (state) => ({
  items: state.timeSeries.get('timeSeries')
})

const mapDispatchToProps = (dispatch) => ({
  editTimeSeries: (timeSeries) => {
    dispatch(editTimeSeries(timeSeries))
  },
  cleanPropertyName: (name) => {
    cleanPropertyName(name)
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeSeriesList)
