/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {connect} from 'react-redux'
import Suggestions from './Suggestions.react'

import * as actions from '../../actions/suggestions'
import {addTimeSeries} from '../../actions/timeSeries'

const mapStateToProps = (state) => ({
  suggestions: state.suggestions
})

const mapDispatchToProps = (dispatch) => ({
  setSuggestionQuery: (query) => {
    dispatch(actions.setSuggestionQuery(query))
  },
  selectSystem: (system) => {
    dispatch(actions.selectSystem(system))
  },
  clearSystem: () => {
    dispatch(actions.clearSystem())
  },
  selectProperty: (property) => {
    dispatch(actions.selectProperty(property))
  },
  clearProperty: () => {
    dispatch(actions.clearProperty())
  },
  selectSpace: (space) => {
    dispatch(actions.selectSpace(space))
  },
  clearSpace: () => {
    dispatch(actions.clearSpace())
  },
  addTimeseries: (agentDescriptions) => {
    dispatch(addTimeSeries(agentDescriptions))
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Suggestions)
