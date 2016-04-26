/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'

import Options from '../options/options.react'
import Suggestions from '../suggestions/suggestions.react'
import TimeWindow from '../time-window/time-window.react'
import EditTimeSeries from '../time-series/time-series-edit.react'

import * as Panels from '../../actions/drawer.constants'

require('./Drawer.styl')

export default class DrawerPanel extends React.Component {

  render() {
    const {activePanel} = this.props

    switch (activePanel) {
      case null:
        return <div />

      case Panels.SUGGESTIONS:
        return <Suggestions />

      case Panels.TIME_WINDOW:
        return <TimeWindow />

      case Panels.ADD_TIMESERIES:
        // fall through

      case Panels.EDIT_TIMESERIES:
        return <EditTimeSeries />

      case Panels.OPTIONS:
        return <Options />

      default:
        return (<div>Warning: Unsupported panel: {activePanel} </div>)
    }
  }
}

DrawerPanel.propTypes = {
  activePanel: PropTypes.object
}
