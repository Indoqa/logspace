/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import Immutable from 'immutable'

import {register,waitFor} from '../dispatcher'

import {viewCursor} from '../state'
import * as Panels from './constants'

import * as timeWindowActions from '../time-window/actions'
import * as timeSeriesActions from '../time-series/actions'
import * as suggestionsActions from '../suggestions/actions'
import * as optionsActions from '../options/actions'
import * as resultActions from '../result/actions'
import * as drawerActions from './actions'

export const DrawerStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case suggestionsActions.onShowSuggestions:
      setActivePanel(Panels.SUGGESTIONS)
      break

    case timeWindowActions.onShowTimeWindowForm:
      setActivePanel(Panels.TIME_WINDOW)
      break

    case timeSeriesActions.onNewTimeSeries:
      setActivePanel(Panels.ADD_TIMESERIES)
      break

    case timeSeriesActions.onEditTimeSeries:
      setActivePanel(Panels.EDIT_TIMESERIES)
      break

    case optionsActions.onShowOptions:
      setActivePanel(Panels.OPTIONS)
      break

    case timeWindowActions.onTimeWindowChange:
      setActivePanel(null)
      break

    case drawerActions.onCloseDrawer:
      setActivePanel(null)
      break

    case timeSeriesActions.onTimeSeriesSaved:
      setActivePanel(null)
      break

    case timeSeriesActions.onTimeSeriesDeleted:
      setActivePanel(null)
      break

    case resultActions.refreshResult:
      setActivePanel(null)
      break
  }
})

function setActivePanel(panel) {
  if(panel === viewCursor().get('activePanel')) {
    panel = null
  }
  const showPanel = panel !== null

  viewCursor(view => {
    return view.updateIn(['navDrawerCss', 'navigation-drawer-expanded'], (value) => {return showPanel})
  })

  viewCursor(view => {
    return view.updateIn(['mainCss', 'main-reduced'], (value) => {return showPanel})
  })

  viewCursor(view => {
    return view.set('activePanel', panel)
  })
}
