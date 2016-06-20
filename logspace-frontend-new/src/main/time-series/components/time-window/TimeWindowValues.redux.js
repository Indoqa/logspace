/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
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
