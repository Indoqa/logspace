/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'

import Component from '../components/component.react'
import Chart from './result-chart.react'
import Header from './result-header.react'

export default class Result extends Component {

  render() {
    return (
      <div>
        <Header 
          chartTitle={this.props.chartTitle}
          chartTitleEditable={this.props.chartTitleEditable}
          chartType={this.props.chartType}
        />  
        <Chart 
          series={this.props.timeSeries} 
          result={this.props.result} 
          chartType={this.props.chartType}
        />
      </div>
    )
  }
}
