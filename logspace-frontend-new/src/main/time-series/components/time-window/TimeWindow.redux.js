/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {connect} from 'react-redux'
import TimeWindow from './TimeWindow.react'

import {showTimeWindowForm, selectPredefinedDate} from '../../actions/timeWindow'
import {selectDynamicDate, selectCustomDate, showTimeWindowTab} from '../../actions/timeWindow'
import {refreshResult} from '../../actions/result'

const mapStateToProps = (state) => ({
  timeWindow: state.timeWindow.get('selection'),
  dynamic: state.timeWindow.get('dynamic'),
  activeTab: state.timeWindow.get('activeTab')
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
  },
  openTab: (index) => {
    dispatch(showTimeWindowTab(index))
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeWindow)
