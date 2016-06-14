import {connect} from 'react-redux'
import TimeSeriesFilter from './TimeSeriesFilter.react'
import * as actions from '../../actions/timeSeries'

const mapStateToProps = (state) => ({
  timeSeries: state.timeSeries.get('timeSeries'),
  editedTimeSeries: state.timeSeries.get('editedTimeSeries'),
  filterValue: state.timeSeries.get('filterValue'),
  filterOptions: state.timeSeries.get('filterOptions'),
  selectedFilters: state.timeSeries.get('selectedFilters')
})

const mapDispatchToProps = (dispatch) => ({
  changeTimeseriesFilter: (value) => {
    dispatch(actions.changeTimeseriesFilter(value))
  },
  changeTimeseriesFilterValue: (input) => {
    dispatch(actions.changeTimeseriesFilterValue(input))
  },
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeSeriesFilter)
