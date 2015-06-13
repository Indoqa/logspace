/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import moment from 'moment'

import {cleanPropertyName} from '../time-series/time-series-item.react'

import * as timeSeriesActions from '../time-series/actions'

export function transformLogspaceResult(timeSeries, responseJson) {
  // data is stored in c3js ready format, see http://c3js.org/reference.html#api-load
  const chartData = {
    colors: {},
    columns: [],
    columnKeys: [],
    originalColumns: {},
    axes: {},
    axisRanges: {
      min: {y:0, y2:0},
      max: {y:0, y2:0}
	  },
    types: {},
    names: {},
    xvalues: createXAxisLabals(responseJson),
    xgap: responseJson.dateRange.gap
  }

  timeSeries.forEach((item, index) => {
    // meta data
    chartData.columnKeys.push(item.get("id"))
    chartData.colors[item.get("id")] = item.get("color")
    chartData.names[item.get("id")] = item.get("name") + ': ' + item.get("aggregate") + " of " + cleanPropertyName(item.get("propertyId"))

    // type
    const typeArray = chartData.types[item.get("type")];
    if (typeArray == null) {
      chartData.types[item.get("type")] = [];
    }

    chartData.types[item.get("type")].push(item.get("id"));

    // apply scale
    const scale = getTimeSeriesScale(item.get("scaleType"), item.get("scaleMin"), item.get("scaleMax"), responseJson.data[index])
    let normalizer;

    if (chartData.axisRanges.max.y == 0) {
      chartData.axisRanges.min.y = scale.min
      chartData.axisRanges.max.y = scale.max
      chartData.axes[item.get("id")] = 'y';
      timeSeriesActions.onAxisChanged(item.get("id"), 'y1')

    } else if (chartData.axisRanges.min.y == scale.min && chartData.axisRanges.max.y == scale.max)  {
      chartData.axes[item.get("id")] = 'y'
      timeSeriesActions.onAxisChanged(item.get("id"), 'y1')

    } else if (chartData.axisRanges.max.y2 == 0) {
      chartData.axisRanges.min.y2 = scale.min
      chartData.axisRanges.max.y2 = scale.max
      chartData.axes[item.get("id")] = 'y2'
      timeSeriesActions.onAxisChanged(item.get("id"), 'y2')

    } else if (chartData.axisRanges.min.y2 == scale.min && chartData.axisRanges.max.y2 == scale.max)  {
      chartData.axes[item.get("id")] = 'y2'
      timeSeriesActions.onAxisChanged(item.get("id"), 'y2')

    } else {
      chartData.axes[item.get("id")] = 'y'
      timeSeriesActions.onAxisChanged(item.get("id"), 'y*')

      normalizer = (value) => {
        const onePercentOfOriginal = (scale.max - scale.min) / 100
        const percentOfOriginal = (value - scale.min) / onePercentOfOriginal
        const targetRange = chartData.axisRanges.max.y - chartData.axisRanges.min.y
        const onePercentOfTarget = targetRange / 100
        // console.log(targetRange + "/" + onePercentOfTarget + "/" + onePercentOfOriginal + "/" + scale.max + "/" + scale.min)
        return onePercentOfTarget * percentOfOriginal
      }
    }

    // original (not normalized y values)
    chartData.originalColumns[item.get("id")] = responseJson.data[index]

    // normalized column / data series (first entry = column key)
    chartData.columns.push(getDataColumn(responseJson.data[index], item.get("id"), normalizer))
  })

  // add 'x' values as column (first entry = column key)
  const xColumn = chartData.xvalues.slice()
  xColumn.unshift('x')
  chartData.columns.unshift(xColumn)

  return chartData
}

function createXAxisLabals(responseJson) {
  let labels = []

  const start = new Date(responseJson.dateRange.start).getTime()
  const end = new Date(responseJson.dateRange.end).getTime()
  const gap = responseJson.dateRange.gap

  for (let i = start; i < end; i = i + (gap * 1000)) {
    labels.push(moment.utc(new Date(i)))
  }

  return labels
}

function getTimeSeriesScale(scaleType, scaleMin, scaleMax, data) {
  if (scaleType === 'auto') {
    return { min: Math.min.apply(Math, data), max: Math.max.apply(Math, data) }
  }

  return { min: parseInt(scaleMin), max: parseInt(scaleMax) }
}


function getDataColumn(array, id, normalizer) {
  const values =  array.map(function(item) {
     if (item != null && normalizer != null) {
       return normalizer(item)
     }

     if (item != null) {
       return item
     }

     return null
   })

   // add id as first value of data array, see http://c3js.org/reference.html#data-columns
   values.unshift(id)

  return values
}
