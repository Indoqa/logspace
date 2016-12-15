/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import classnames from 'classnames'

import Drawer from './drawer/Drawer.redux'
import AddTimeSeries from './time-series/TimeSeriesAdd.redux'
import ClearTimeSeries from './time-series/TimeSeriesClear.redux'
import TimeSeriesList from './time-series/TimeSeriesList.redux'
import TimeWindowValues from './time-window/TimeWindowValues.redux'
import Result from './result/Result.redux'
import Header from './Header.redux'

import BasePage from '../../app/components/BasePage.react'

import * as routes from '../../routes'

require('./Page.styl')

const BREADCRUMBS = [{children: 'Reports', to: routes.ROUTE_REPORTS}, {children: 'REPORT'}]

export default class TimeSeries extends React.Component {

  componentDidMount() {
    this.props.initialize()
  }

  render() {
    const {mainCss, navDrawerCss} = this.props

    const header = (
      <Header
        autoPlay={this.props.autoPlay}
        chartTitle={this.props.chartTitle}
        chartTitleEditable={this.props.chartTitleEditable}
        chartType={this.props.chartType}
        updateEditableState={this.props.updateEditableState}
        saveChartTitle={this.props.saveChartTitle}
        setChartType={this.props.setChartType}
        refreshResult={this.props.refreshResult}
        toggleAutoPlay={this.props.toggleAutoPlay}
      />)

    return (
      <BasePage title="Report" breadcrumbs={BREADCRUMBS} headerComponents={header}>
        <div className="time-series">
          <div className={classnames(navDrawerCss.toJS())}>
            <div className="left">
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
      </BasePage>
    )
  }
}

TimeSeries.propTypes = {
  mainCss: PropTypes.object.isRequired,
  navDrawerCss: PropTypes.object.isRequired,
  initialize: PropTypes.func.isRequired,

  autoPlay: PropTypes.object.isRequired,
  chartType: PropTypes.string.isRequired,
  chartTitle: PropTypes.string.isRequired,
  chartTitleEditable: PropTypes.object,
  updateEditableState: PropTypes.func.isRequired,
  saveChartTitle: PropTypes.func.isRequired,
  setChartType: PropTypes.func.isRequired,
  refreshResult: PropTypes.func.isRequired,
  toggleAutoPlay: PropTypes.func.isRequired
}
