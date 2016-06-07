/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {fromJS, Record} from 'immutable'

import * as Panels from '../actions/drawer.constants'
import * as timeWindowActions from '../actions/timeWindow'
import * as timeSeriesActions from '../actions/timeSeries'
import * as suggestionsActions from '../actions/suggestions'
import * as optionsActions from '../actions/options'
import * as resultActions from '../actions/result'
import * as drawerActions from '../actions/drawer'

const InitialState = Record({
  activePanel: null,
  navDrawerCss: fromJS({
    'navigation-drawer': true,
    'navigation-drawer-expanded': false
  }),
  mainCss: fromJS({
    main: true,
    'main-reduced': false
  })
})

const setActivePanel = (state, panel, omitClose = false) => {
  if (!omitClose && panel === state.get('activePanel')) {
    panel = null
  }
  const showPanel = panel !== null

  return state.
    setIn(['navDrawerCss', 'navigation-drawer-expanded'], showPanel).
    setIn(['mainCss', 'main-reduced'], showPanel).
    set('activePanel', panel)
}

export default (state = new InitialState, action) => {
  switch (action.type) {
    case suggestionsActions.SHOW_SUGGESTIONS:
      return setActivePanel(state, Panels.SUGGESTIONS)

    case timeWindowActions.SHOW_TIMEWINDOW_FORM:
      return setActivePanel(state, Panels.TIME_WINDOW)

    case timeSeriesActions.ADD_TIMESERIES:
      return setActivePanel(state, Panels.ADD_TIMESERIES)

    case timeSeriesActions.EDIT_TIMESERIES:
      return setActivePanel(state, Panels.EDIT_TIMESERIES, true)

    case optionsActions.SHOW_OPTIONS:
      return setActivePanel(state, Panels.OPTIONS)

    case timeWindowActions.SELECT_CUSTOM_DATE:
      return setActivePanel(state, null)

    case timeWindowActions.SELECT_PREDEFINED_DATE:
      return setActivePanel(state, null)

    case timeWindowActions.SELECT_DYNAMIC_DATE:
      return setActivePanel(state, null)

    case drawerActions.CLOSE_DRAWER:
      return setActivePanel(state, null)

    case timeSeriesActions.SAVE_TIMESERIES:
      return setActivePanel(state, null)

    case timeSeriesActions.DELETE_TIMESERIES:
      return setActivePanel(state, null)

    case resultActions.REFRESH_RESULT:
      return setActivePanel(state, null)

    default:
      return state
  }
}
