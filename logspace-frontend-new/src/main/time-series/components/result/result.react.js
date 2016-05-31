/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'

import Chart from './result-chart.react'
import Header from './result-header.react'
import Footer from './result-footer.react'

export default class Result extends React.Component {

  render() {
    return (
      <div>
        <Header
          autoPlay={this.props.autoPlay}
          autoPlaySchedule={this.props.autoPlaySchedule}
          chartTitle={this.props.chartTitle}
          chartTitleEditable={this.props.chartTitleEditable}
          chartType={this.props.chartType}
          updateEditableState={this.props.updateEditableState}
          saveChartTitle={this.props.saveChartTitle}
          setChartType={this.props.setChartType}
          refreshResult={this.props.refreshResult}
          setAutoPlay={this.props.setAutoPlay}
        />
        <Chart
          series={this.props.timeSeries}
          result={this.props.result}
          chartType={this.props.chartType}
        />
        <Footer result={this.props.result} />
      </div>
    )
  }
}

Result.propTypes = {
  autoPlay: PropTypes.bool.isRequired,
  autoPlaySchedule: PropTypes.object,
  chartType: PropTypes.string.isRequired,
  chartTitle: PropTypes.string.isRequired,
  chartTitleEditable: PropTypes.object.isRequired,
  result: PropTypes.object.isRequired,
  timeSeries: PropTypes.object.isRequired,
  updateEditableState: PropTypes.func.isRequired,
  saveChartTitle: PropTypes.func.isRequired,
  setChartType: PropTypes.func.isRequired,
  refreshResult: PropTypes.func.isRequired,
  setAutoPlay: PropTypes.func.isRequired
}
