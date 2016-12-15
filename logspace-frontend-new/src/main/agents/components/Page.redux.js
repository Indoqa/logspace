/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {connect} from 'react-redux'
import Page from './Page.react'

import * as agentsActions from '../actions/agents'

const mapStateToProps = (state) => ({
  agentActivities: state.agents.get('agentActivities'),
  maxHistoryValue: state.agents.get('maxHistoryValue'),
  sort: state.agents.get('sort'),
  loading: state.agents.get('loading'),
})

const mapDispatchToProps = (dispatch) => ({
  loadAgentActivities: () => {
    dispatch(agentsActions.loadAgentActivities())
  },

  setSort: (sort) => {
    dispatch(agentsActions.setSort(sort))
  },
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Page)
