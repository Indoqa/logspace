/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
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
