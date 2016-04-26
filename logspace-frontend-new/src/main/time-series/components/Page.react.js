/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import classnames from 'classnames'

import AddTimeSeries from './time-series/time-series-add.react'
import ClearTimeSeries from './time-series/time-series-clear.react'
import TimeSeriesList from './time-series/time-series-list.react'
import TimeWindowValues from './time-window/time-window-values.react'
import Result from './result/result.react'
import Drawer from './drawer/Drawer.react'
import Header from './header/header.react'

require('./Page.styl')

export default class TimeSeries extends React.Component {

  componentDidMount() {
    this.props.initialize()
  }

  render() {
    const view = this.props

    return (
      <div className="time-series">
        <div className={classnames(view.get('navDrawerCss').toJS())}>
          <div className="left">
            <Header />
            <TimeWindowValues />
            <div className="time-series-list-wrapper">
              <TimeSeriesList />
              <ClearTimeSeries />
              <AddTimeSeries />
            </div>
          </div>

          <div className="right">
            <Drawer />
          </div>

        </div>

        <div className={classnames(view.get('mainCss').toJS())}>
          <Result />
          />
        </div>
      </div>
    )
  }
}

TimeSeries.propTypes = {
  view: PropTypes.object,
  initialize: PropTypes.func.isRequired
}
