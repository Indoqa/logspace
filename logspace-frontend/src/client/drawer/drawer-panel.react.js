/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'

import Component from '../components/component.react'
import Options  from '../options/options.react'
import Suggestions  from '../suggestions/suggestions.react'
import TimeWindow  from '../time-window/time-window.react'
import EditTimeSeries from '../time-series/time-series-edit.react'

import * as Panels from './constants'

require('./drawer.styl')

export default class DrawerPanel extends Component {

  render() {
    switch(this.props.view.get('activePanel')) {
      case null:
        return <div/>

      case Panels.SUGGESTIONS:
        return <Suggestions suggestions={this.props.suggestions}/>

      case Panels.TIME_WINDOW:
        return <TimeWindow timeWindow={this.props.timeWindow} activeTab={this.props.timeWindowActiveTab} />

      case Panels.ADD_TIMESERIES:
        // fall through

      case Panels.EDIT_TIMESERIES:
        return <EditTimeSeries editedTimeSeries={this.props.editedTimeSeries} timeSeries={this.props.timeSeries}/>

      case Panels.OPTIONS:
        return <Options  chartTitle={this.props.chartTitle}/>

      default:
        return (<div>Warning: Unsupported panel: {this.props.activePanel} </div>)
    }
  }
}
