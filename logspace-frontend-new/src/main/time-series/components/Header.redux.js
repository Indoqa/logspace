/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {connect} from 'react-redux'
import Header from './Header.react'

import * as resultActions from '../actions/result'
import * as editableActions from '../../app/actions/editable'

const mapStateToProps = (state) => ({
  autoPlay: state.result.get('autoPlay'),
  loading: state.result.get('loading'),
  chartType: state.result.get('chartType'),
  chartTitle: state.result.get('chartTitle'),
  chartTitleEditable: state.editable.get('result'),
})

const mapDispatchToProps = (dispatch) => ({
  updateEditableState: (id, name, state) => {
    dispatch(editableActions.updateEditableState(id, name, state))
  },
  saveChartTitle: (title) => {
    dispatch(resultActions.saveChartTitle(title))
  },
  setChartType: (type) => {
    dispatch(resultActions.setChartType(type))
  },
  refreshResult: () => {
    dispatch(resultActions.refreshResult())
  },
  toggleAutoPlay: () => {
    dispatch(resultActions.toggleAutoPlay())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Header)
