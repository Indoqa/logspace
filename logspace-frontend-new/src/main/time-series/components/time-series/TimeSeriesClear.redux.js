/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {connect} from 'react-redux'
import TimeSeriesClear from './TimeSeriesClear.react'

import {resetTimeSeries} from '../../actions/timeSeries'
import {refreshResult} from '../../actions/result'

const mapStateToProps = (state) => ({
  count: state.timeSeries.get('timeSeries').size
})

const mapDispatchToProps = (dispatch) => ({
  resetTimeSeries: () => {
    dispatch(resetTimeSeries())
  },
  refreshResult: () => {
    dispatch(refreshResult())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeSeriesClear)
