/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import Immutable from 'immutable'
import c3 from 'c3'
import classnames from 'classnames'
import Halogen from 'halogen'

import debounceFunc from '../../../app/utils/debounce'
import {units} from '../../actions/timeWindow.constants'
import {marginTop} from '../../../environment'

require('./ResultChart.styl')

const ComponentState = Immutable.fromJS({
  loadingCss: {
    loading: true,
    active: false
  }
})

export default class Chart extends React.Component {

  constructor(props) {
    super(props)

    this.state = {
      localState: ComponentState
    }

    this.prevType = props.chartType
  }

  componentWillReceiveProps(nextProps) {
    this.rememberPreviousChartType(this.props.chartType)

    if (nextProps.result.get('loading')) {
      this.toggleLoading(true)
      return
    }

    this.toggleLoading(false)
  }

  componentDidUpdate() {
    const messageElement = document.getElementById('message')
    messageElement.innerHTML = ''

    if (this.props.result.get('loading')) {
      return
    }

    if (this.props.result.get('error')) {
      this.clearChart()
      messageElement.innerHTML = `${this.props.result.get('errorStatus')}<br/><small>${this.props.result.get('errorText')}</small>`
      return
    }

    if (this.props.result.get('empty')) {
      this.clearChart()
      messageElement.innerHTML = 'Empty Chart<br/><small>Add at least one time series</small>'
      return
    }

    if (this.prevType !== this.props.chartType) {
      this.transform(this.props.chartType)
      this.prevType = this.props.chartType
      return
    }

    this.clearChart()

    const chartData = this.props.result.get('chartData').toJS()

    if (this.isEmpty(chartData)) {
      this.clearChart()
      messageElement.innerHTML = 'Empty Chart<br/><small>No data found in selected time window</small>'
      return
    }

    this.chart = c3.generate(this.chartOptions(chartData))
    this.originalColumns = chartData.originalColumns
  }

  isEmpty(chartData) {
    const originalColumns = chartData.originalColumns
    let count = 0

    for (const key in originalColumns) {
      if ({}.hasOwnProperty(originalColumns, key)) {
        const originalColumn = originalColumns[key]
        count = count + originalColumn.length
      }
    }

    return count === 0
  }

  toggleLoading(show) {
    this.setState({
      localState: this.state.localState.updateIn(['loadingCss', 'active'], () => show)
    })
  }

  rememberPreviousChartType(type) {
    this.prevType = type
  }

  clearChart() {
    if (this.chart) {
      try { this.chart.destroy() } catch (e) {/**/}
    }
  }

  calculateChartSize() {
    const environmentMarginTop = marginTop()

    const windowWidth = Math.max(document.documentElement.clientWidth, window.innerWidth || 0)
    const windowHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0) - environmentMarginTop

    const minWindowWidth = 1024

    const headerheight = 40
    const sidebarWidth = 250
    const horizontalPadding = 20
    const verticalPadding = 50

    const width = Math.max(windowWidth, minWindowWidth) - sidebarWidth - horizontalPadding
    const height = windowHeight - headerheight - verticalPadding

    return {
      width, height
    }
  }

  resizeChart() {
    this.chart.resize(this.calculateChartSize())
  }

  transform(type) {
    this.chart.transform(type)
  }

  formatXAxis(index) {
    const date = this.props.result.get('chartData').get('xvalues').get(index)

    if (!date) {
      return null
    }

    const unit = this.props.result.get('gap').get('unit')

    switch (unit.get('id')) {
      case units.get('second').get('id'):
        return date.format('HH:mm:ss')

      case units.get('minute').get('id'):
        return date.format('HH:mm')

      case units.get('hour').get('id'):
        return date.format('HH:00')

      case units.get('day').get('id'):
        return date.format('DD.MM.')

      case units.get('week').get('id'):
        return date.format('DD.MM.')

      case units.get('month').get('id'):
        return date.format('MMMM')

      case units.get('year').get('id'):
        return date.format('YYYY')

      default:
        return unit
    }
  }

  formatXTooltip(index) {
    const date = this.props.result.get('chartData').get('xvalues').get(index)

    if (!date) {
      return null
    }

    const unit = this.props.result.get('gap').get('unit')

    switch (unit.get('id')) {
      case units.get('second').get('id'):
        return date.format('dd DD.MM.YYYY - HH:mm:ss')

      case units.get('minute').get('id'):
        return date.format('dd DD.MM.YYYY - HH:mm')

      case units.get('hour').get('id'):
        return date.format('dd DD.MM.YYYY - HH:00')

      case units.get('day').get('id'):
        return date.format('dd DD.MM.YYYY')

      case units.get('week').get('id'):
        return date.format('dd DD.MM.YYYY')

      case units.get('month').get('id'):
        return date.format('MMMM YYYY')

      case units.get('year').get('id'):
        return date.format('YYYY')

      default:
        return unit
    }

  }

  formatYTooltip(value, ratio, id, index) {
    const yValue = this.originalColumns[id][index]
    return yValue
  }

  formatYTick(value) {
    return value
  }

  getMaxTicks(chartData) { // eslint-disable-line react/sort-comp
    if (chartData.xvalues.length < 10) {
      return chartData.xvalues.length
    }

    return 10
  }

  chartOptions(chartData) {
    const chartSize = this.calculateChartSize()
    const me = this

    const debouncedChartResize = debounceFunc(this.resizeChart.bind(me), 350)
    const formatXAxisCallback = this.formatXAxis.bind(me)
    const formatXTooltipCallback = this.formatXTooltip.bind(me)
    const formatYTooltipCallback = this.formatYTooltip.bind(me)
    const formatYTick = this.formatYTick.bind(me)

    const defaultType = this.props.chartType


    return {
      data: {
        x: 'x',
        type: defaultType,
        colors: chartData.colors,
        columns: chartData.columns,
        names: chartData.names,
        axes: chartData.axes
      },
      axis: {
        x: {
          type: 'category',
          padding: {
            left: 0,
            right: 3,
          },
          tick: {
            count: this.getMaxTicks(chartData),
            fit: true,
            height: 130,
            rotate: 0,
            format: formatXAxisCallback
          }
        },
        y: {
          show: true,
          padding: {
            top: 5,
            bottom: 5
          },
          tick: {
            format: formatYTick
          },
          min: chartData.axisRanges.min.y,
          max: chartData.axisRanges.max.y
        },
        y2: this.getY2Options(chartData, formatYTick)
      },
      grid: {
        x: {
          show: false
        },
        y: {
          show: false
        }
      },
      tooltip: {
        format: {
          title: formatXTooltipCallback,
          value: formatYTooltipCallback
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

  render() {
    return (
      <div className={'resultChart'}>
        <div className={classnames(this.state.localState.get('loadingCss').toJS())}>
          <span>
            <Halogen.PulseLoader color={'#ddfcff'} size={'50px'} />
          </span>
        </div>
        <div className={'message'} id="message" />
        <div id="chart" />
      </div>
    )
  }

  getY2Options(chartData, formatYTick) {
    if (chartData.axisRanges.min.y2 === 0 && chartData.axisRanges.max.y2 === 0) {
      return {
        show: false
      }
    }

    return {
      show: true,
      padding: {
        top: 5,
        bottom: 10
      },
      tick: {
        format: formatYTick
      },
      min: chartData.axisRanges.min.y2,
      max: chartData.axisRanges.max.y2
    }
  }
}

Chart.propTypes = {
  chartType: PropTypes.string.isRequired,
  result: PropTypes.object.isRequired
}
