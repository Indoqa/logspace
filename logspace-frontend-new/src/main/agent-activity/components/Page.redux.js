/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {connect} from 'react-redux'
import Page from './Page.react'

import * as agentActivityActions from '../actions/agentActivity'

const mapStateToProps = (state) => ({
  agentActivities: state.agentActivity.get('agentActivities'),
  maxHistoryValue: state.agentActivity.get('maxHistoryValue'),
  sort: state.agentActivity.get('sort'),
  loading: state.agentActivity.get('loading'),
})

const mapDispatchToProps = (dispatch) => ({
  loadAgentActivities: () => {
    dispatch(agentActivityActions.loadAgentActivities())
  },

  setDuration: (duration) => {
    dispatch(agentActivityActions.setDuration(duration))
  },

  setSort: (sort) => {
    dispatch(agentActivityActions.setSort(sort))
  },
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Page)
