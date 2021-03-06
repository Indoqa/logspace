/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import moment from 'moment'

import {cleanPropertyName} from '../components/time-series/TimeSeriesItem.react'
import {isSubitem, getReference} from './timeSeries.constants'

function createXAxisLabals(responseJson) {
  const labels = []

  const start = new Date(responseJson.timeWindow.start).getTime()
  const end = new Date(responseJson.timeWindow.end).getTime()
  const gap = responseJson.timeWindow.gap

  for (let i = start; i < end; i = i + (gap * 1000)) {
    labels.push(moment(new Date(i)))
  }

  return labels
}

function getTimeSeriesScale(scaleType, scaleMin, scaleMax, data) {
  const scale = {
    min: null,
    max: null
  }

  if (scaleType === 'auto') {
    scale.min = Math.min.apply(Math, data)
    scale.max = Math.max.apply(Math, data)
  } else {
    scale.min = parseInt(scaleMin, 0)
    scale.max = parseInt(scaleMax, 0)
  }

  if (scale.min === scale.max) {
    scale.min = scale.min * 0.9
    scale.max = scale.max * 1.1
  }

  return scale
}

function createScaleMap(items, responseJson) {
  const map = []

  items.forEach((item, index) => {
    map[item.get('id')] = getTimeSeriesScale(
      item.get('scaleType'),
      item.get('scaleMin'),
      item.get('scaleMax'),
      responseJson.data[index]
    )
  })

  return map
}

function getAppliedScale(item, scaleMap) {
  const scaleType = item.get('scaleType')

  if (!isSubitem(scaleType)) {
    return scaleMap[item.get('id')]
  }

  const reference = getReference(scaleType)
  return scaleMap[reference]
}

function getDataColumn(array, id, normalizer) {
  const values = array.map((item) => {
    if (item && normalizer) {
      return normalizer(item)
    }

    if (item) {
      return item
    }

    return null
  })

  // add id as first value of data array, see http://c3js.org/reference.html#data-columns
  values.unshift(id)

  return values
}


export function transformLogspaceResult(timeSeries, responseJson) {
  // data is stored in c3js ready format, see http://c3js.org/reference.html#api-load
  const chartData = {
    colors: {},
    columns: [],
    columnKeys: [],
    originalColumns: {},
    axes: {},
    axisRanges: {
      min: {y: 0, y2: 0},
      max: {y: 0, y2: 0}
    },
    types: {},
    names: {},
    xvalues: createXAxisLabals(responseJson)
  }

  const scaleMap = createScaleMap(timeSeries, responseJson)

  timeSeries.forEach((item, index) => {
    // meta data
    chartData.columnKeys.push(item.get('id'))
    chartData.colors[item.get('id')] = item.get('color')
    chartData.names[item.get('id')] = `${item.get('name')}: ${item.get('aggregate')} of ${cleanPropertyName(item.get('propertyId'))}`

    // type
    const typeArray = chartData.types[item.get('type')]
    if (!typeArray) {
      chartData.types[item.get('type')] = []
    }

    chartData.types[item.get('type')].push(item.get('id'))

    // apply scale
    const scale = getAppliedScale(item, scaleMap)

    let normalizer
    const axisRanges = chartData.axisRanges
    if (axisRanges.max.y === 0) {
      chartData.axisRanges.min.y = scale.min
      chartData.axisRanges.max.y = scale.max
      chartData.axes[item.get('id')] = 'y'

    } else if (isSubitem(item.get('scaleType')) && axisRanges.min.y === scale.min && axisRanges.max.y === scale.max) {
      chartData.axes[item.get('id')] = 'y'

    } else if (axisRanges.max.y2 === 0) {
      chartData.axisRanges.min.y2 = scale.min
      chartData.axisRanges.max.y2 = scale.max
      chartData.axes[item.get('id')] = 'y2'

    } else if (axisRanges.min.y2 === scale.min && axisRanges.max.y2 === scale.max) {
      chartData.axes[item.get('id')] = 'y2'

    } else {
      chartData.axes[item.get('id')] = 'y'

      normalizer = (value) => {
        const targetOffset = chartData.axisRanges.min.y
        const targetRange = chartData.axisRanges.max.y - targetOffset
        const onePercentOfTarget = targetRange / 100

        const sourceRange = scale.max - scale.min

        // constant value for all slots in this series -> map line to 50%
        if (sourceRange === 1) {
          return onePercentOfTarget * 50 + targetOffset
        }

        const onePercentOfOriginal = sourceRange / 100
        const percentOfOriginal = (value - scale.min) / onePercentOfOriginal

        return onePercentOfTarget * percentOfOriginal + targetOffset
      }
    }

    // original (not normalized y values)
    chartData.originalColumns[item.get('id')] = responseJson.data[index]

    // normalized column / data series (first entry = column key)
    chartData.columns.push(getDataColumn(responseJson.data[index], item.get('id'), normalizer))
  })

  // add 'x' values as column (first entry = column key)
  const xColumn = chartData.xvalues.slice()
  xColumn.unshift('x')
  chartData.columns.unshift(xColumn)

  return chartData
}
