/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import classnames from 'classnames'

import Header from './header/Header.redux'
import Drawer from './drawer/Drawer.redux'
import AddTimeSeries from './time-series/TimeSeriesAdd.redux'
import ClearTimeSeries from './time-series/TimeSeriesClear.redux'
import TimeSeriesList from './time-series/TimeSeriesList.redux'
import TimeWindowValues from './time-window/TimeWindowValues.redux'
import Result from './result/Result.redux'

require('./Page.styl')

export default class TimeSeries extends React.Component {

  componentDidMount() {
    this.props.initialize()
  }

  render() {
    const {mainCss, navDrawerCss} = this.props

    return (
      <div className="time-series">
        <div className={classnames(navDrawerCss.toJS())}>
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

        <div className={classnames(mainCss.toJS())}>
          <Result />
        </div>
      </div>
    )
  }
}

TimeSeries.propTypes = {
  mainCss: PropTypes.object.isRequired,
  navDrawerCss: PropTypes.object.isRequired,
  initialize: PropTypes.func.isRequired
}
