/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import Immutable from 'immutable'
import c3 from 'c3'
import classnames from 'classnames'
import Halogen from 'halogen'
import moment from 'moment'
import shallowEqual from 'react-pure-render/shallowEqual';

import Component from '../components/component.react'
import debounceFunc from '../../lib/debounce'

import {GAPS} from '../time-window/constants'

import {onResultRefreshed} from './actions'

require ('./result-chart.styl')

const ComponentState = Immutable.fromJS({
  loadingCss: {
    'loading' : true,
    'active' : false
  },
  type: 'spline'
})

export default class Chart extends Component {

  constructor(props) {
    super(props)
    this.state = {
      localState: ComponentState
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.result.get("loading")) {
      this.toggleLoading(true)
      return
    }

    this.toggleLoading(false)
  }


  componentDidMount() {
    this.chart = c3.generate(this.chartOptions())
  }

  componentWillUnmount() {
    this.chart.destroy()
  }

  componentDidUpdate() {
    if (this.props.result.get("loading")) {
      return
    }

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
        axes: chartData.axes,
        unload: keysToUnload
      }
    )

    this.chart.data.names(chartData.names)
    this.chart.axis.range(chartData.axisRanges)

    this.originalColumns = chartData.originalColumns
  }

  toggleLoading(show) {
    this.setState({
      localState: this.state.localState.updateIn(['loadingCss', 'active'], () => { return show })
    })
  }

  calculateChartSize() {
    const windowWidth = Math.max(document.documentElement.clientWidth, window.innerWidth || 0)
    const windowHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0)

    const minWindowWidth = 1024
    const minWindowHeight = windowHeight - 200

    const sidebarWidth = 250
    const chartPadding = 20 * 2
    const heightWidthRatio = 0.45

    const width = Math.max(windowWidth, minWindowWidth) - sidebarWidth - chartPadding
    const height = Math.min(width * heightWidthRatio, minWindowHeight)

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
    this.setState({
      localState: this.state.localState.updateIn(['type'], () => { return type })
    })
  }

  formatXAxis(index) {
    const date = this.props.result.get('chartData').get('xvalues').get(index)
    const gap = this.props.result.get('chartData').get("xgap")

    switch (gap) {
      case GAPS.second:
        return date.format('HH:mm:ss')

      case GAPS.minute:
        return date.format('HH:mm')

      case GAPS.hour:
        return date.format('HH:00')

      case GAPS.day:
        return date.format('DD.MM.')

      case GAPS.week:
        return date.format('DD.MM.')

      case GAPS.month:
        return date.format('MMMM')

      case GAPS.year:
        return date.format('YYYY')
    }

    return gap
  }

  formatXTooltip(index) {
    const date = this.props.result.get('chartData').get('xvalues').get(index)
    const gap = this.props.result.get('chartData').get("xgap")

    switch (gap) {
      case GAPS.second:
        return date.format('dd DD.MM.YYYY - HH:mm:ss')

      case GAPS.minute:
        return date.format('dd DD.MM.YYYY - HH:mm')

      case GAPS.hour:
        return date.format('dd DD.MM.YYYY - HH:00')

      case GAPS.day:
        return date.format('dd DD.MM.YYYY')

      case GAPS.week:
        return date.format('dd DD.MM.YYYY')

      case GAPS.month:
        return date.format('MMMM YYYY')

      case GAPS.year:
        return date.format('YYYY')
    }

    return gap
  }

  formatYTooltip(value, ratio, id, index) {
    return this.originalColumns[id][index]
  }

  render() {
    return (
      <div>
       <div className='chart-header'>
        <div className='chart-options'>
           <select onChange={(e) => this.transform(e.target.value)}
                   value={this.state.localState.get('type')} >
            <option value={'bar'}>bar</option>
            <option value={'line'}>line</option>
            <option value={'spline'}>spline</option>
            <option value={'step'}>step</option>
            <option value={'area'}>area</option>
            <option value={'area-spline'}>area-spline</option>
            <option value={'area-step'}>area-step</option>
            <option value={'scatter'}>scatter</option>
          </select>
        </div>
        <div className="chart-title">
          <span className='title'>New logspace.io chart</span>
          <span className='edit'>[edit]</span>
        </div>
        </div>
        <div className={'resultChart'}>
          <div className={classnames(this.state.localState.get('loadingCss').toJS())}>
            <span>
              <Halogen.PulseLoader color={'#BBDEFB'} size={'50px'} />
            </span>
          </div>
          <div id="chart" />
        </div>
      </div>
    )
  }

  chartOptions() {
    const chartSize = this.calculateChartSize()
    const me = this

    const debouncedChartResize = debounceFunc(this.resizeChart.bind(me), 350)
    const formatXAxisCallback = this.formatXAxis.bind(me)
    const formatXTooltipCallback = this.formatXTooltip.bind(me)
    const formatYTooltipCallback = this.formatYTooltip.bind(me)

    const defaultType = this.state.type
    
    return {
      data: {
          x: 'x',
          columns: [],
          type: this.state.localState.get('type')
        },
      axis: {
        x: {
          type: 'category',
          tick: {
            count: 10,
            height: 130,
            rotate: 0,
            format: formatXAxisCallback
          }
        },
        y: {
          show: true,
          label: 'y1'
        },
        y2: {
          show: true,
          label: 'y2'
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
}
