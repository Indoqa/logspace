/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
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
