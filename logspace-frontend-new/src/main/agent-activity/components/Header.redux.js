/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {connect} from 'react-redux'
import Header from './Header.react'

import * as agentActivityActions from '../actions/agentActivity'

const mapStateToProps = (state) => ({
  autoPlay: state.agentActivity.get('autoPlay'),
  duration: state.agentActivity.get('duration'),
})

const mapDispatchToProps = (dispatch) => ({
  setDuration: (duration) => {
    dispatch(agentActivityActions.setDuration(duration))
  },

  toggleAutoPlay: () => {
    dispatch(agentActivityActions.toggleAutoPlay())
  },

  loadAgentActivities: () => {
    dispatch(agentActivityActions.loadAgentActivities())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Header)
