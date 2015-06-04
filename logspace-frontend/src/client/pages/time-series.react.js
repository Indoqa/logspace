/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import {Link} from 'react-router'
import classnames from 'classnames'

import AddTimeSeries  from '../time-series/time-series-add.react'
import TimeSeriesList  from '../time-series/time-series-list.react'
import TimeWindowValues  from '../time-window/time-window-values.react'
import Chart from '../result/result-chart.react'
import Drawer from '../drawer/drawer.react'
import Header from '../header/header.react'

import Component from '../components/component.react'

import {onNewSuggestionQuery} from '../suggestions/actions'
import {onShowTimeWindowForm} from '../time-window/actions'

require('./time-series.styl')

export default class TimeSeries extends Component {

  constructor(props) {
    super(props);
  }

  componentDidMount() {
    onNewSuggestionQuery(null)
  }

  render() {
    return (
      <div className='time-series'>

        <div className={classnames(this.props.view.get('navDrawerCss').toJS())}>

          <div className="left">
            <Header/>
            <TimeWindowValues timeWindow={this.props.timeWindow}/>
            <TimeSeriesList items={this.props.timeSeries} />
            <AddTimeSeries count={this.props.timeSeries.size} />
          </div>

          <div className="right">
            <Drawer
              activePanel={this.props.activePanel}
              suggestions={this.props.suggestions}
              timeWindow={this.props.timeWindow}
              timeSeries={this.props.timeSeries}
              editedTimeSeries={this.props.editedTimeSeries} />
          </div>

        </div>

        <div className={classnames(this.props.view.get('mainCss').toJS())}>
          <Chart series={this.props.timeSeries} result={this.props.result} />
        </div>

      </div>
    )
  }
}
