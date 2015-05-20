/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import c3 from 'c3'

import PureComponent from '../components/purecomponent.react'
import debounceFunc from '../../lib/debounce'

import {onResultRefreshed} from './actions'

require ('./result-chart.css')

export default class Chart extends PureComponent {

  componentDidMount() {
    this.chart = c3.generate(this.chartOptions())
  }

  componentWillUnmount() {
    this.chart.destroy()
  }

  componentDidUpdate() {
    if (this.props.result.get("empty") || this.props.result.get("error")) {
      this.chart.unload()
      return
    }

    const chartData = this.props.result.get("chartData").toJS()
    const currentData = this.chart.data()

    const keysToUnload = currentData.map(function(item) {
      if (chartData.columnKeys.indexOf(item.id) > -1 ) {
        return null
      }
      return item.id
    })

    this.chart.load(
      {
        colors: chartData.colors,
        columns: chartData.columns,
        unload: keysToUnload
      }
    );

/*
    this.chart.xgrids([
      {value: 0, text:chartData.columns[0][1]},
      {value: chartData.columns[0].length - 2, text: chartData.columns[0][chartData.columns[0].length - 1]}
    ])
*/
  }

  calculateChartSize() {
    const windowWidth = Math.max(document.documentElement.clientWidth, window.innerWidth || 0)
    const minWindowWidth = 1024
    const sidebarWidth = 250
    const chartPadding = 20 * 2
    const heightWidthRatio = 0.45

    const width = Math.max(windowWidth, minWindowWidth) - sidebarWidth - chartPadding
    const height = width * heightWidthRatio

    return {
      width : width,
      height : height
    }
  }

  resizeChart() {
    this.chart.resize(this.calculateChartSize())
  }

  transform(type) {
    this.chart.transform(type)
  }

  render() {
    return (
      <div>
        <div id="chart" / >
        <br/>
        <button onClick={() => this.transform('bar')}>bar</button>
        <button onClick={() => this.transform('line')}>line</button>
        <button onClick={() => this.transform('spline')}>spline</button>
        <button onClick={() => this.transform('step')}>step</button>
        <button onClick={() => this.transform('area')}>area</button>
        <button onClick={() => this.transform('area-spline')}>area-spline</button>
        <button onClick={() => this.transform('area-step')}>area-step</button>
        <button onClick={() => this.transform('scatter')}>scatter</button>
      </div>
    )
  }

  chartOptions() {
    const chartSize = this.calculateChartSize()
    const me = this

    const debouncedChartResize = debounceFunc(this.resizeChart.bind(me), 350)

    return {
      data: {
          x: 'x',
          columns: [],
        },
      axis: {
        x: {
          type: 'category',
          tick: {
            culling: true
          }
        },
        y: {
          show: true
        }
      },
      grid: {
        x: {
          show: false
        },
        y: {
          show: false
        }
      },
      legend: {
        show: false
      },
      point: {
        r: 2
      },
      size: {
        height: chartSize.height,
        width: chartSize.width
      },
      onresized: debouncedChartResize
    }
  }
}
