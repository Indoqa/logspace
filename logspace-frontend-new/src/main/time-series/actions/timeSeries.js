/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {fromJS} from 'immutable'

export const CHANGE_TIMESERIES_FILTER = 'CHANGE_TIMESERIES_FILTER'
export const CHANGE_TIMESERIES_FILTER_VALUE = 'CHANGE_TIMESERIES_FILTER_VALUE'
export const CHANGE_TIMESERIES_PROPERTY = 'CHANGE_TIMESERIES_PROPERTY'
export const SAVE_TIMESERIES = 'SAVE_TIMESERIES'
export const ADD_TIMESERIES = 'ADD_TIMESERIES'
export const EDIT_TIMESERIES = 'EDIT_TIMESERIES'
export const DELETE_TIMESERIES = 'DELETE_TIMESERIES'
export const REMEMBER_SELECTED_TIMESERIES_PROPERTY = 'REMEMBER_SELECTED_TIMESERIES_PROPERTY'
export const RESET_TIMESERIES = 'RESET_TIMESERIES'

const getEmptyOptionIndex = (options) => {
  return options.findIndex((option) => {
    return option.get('option') && option.get('input') === null
  })
}

export const changeTimeseriesFilter = (selectedOptions) => {
  let options = fromJS(selectedOptions)
  const emptyOptionIndex = getEmptyOptionIndex(options)
  if (emptyOptionIndex !== -1) {
    const valueIndex = options.findIndex((option) => {return option.get('option') === false})
    if (valueIndex !== -1) {
      const value = options.get(valueIndex)
      const option = options.get(emptyOptionIndex)
      options = options.set(emptyOptionIndex, option.set('input', value.get('label')))
      options = options.delete(valueIndex)
    }
  }
  return {
    type: CHANGE_TIMESERIES_FILTER,
    payload: {filters: options}
  }
}

export const changeTimeseriesFilterValue = (value) => ({store}) => {
  const selectedOptions = store.getState().timeSeries.get('selectedFilters')
  const propertyDescriptions = store.getState().timeSeries.get('editedTimeSeries').get('propertyDescriptions')
  const propertyOptions = propertyDescriptions.toJS().map((description) => (
    {id: description.id, label: description.name, input: null, option: true}
  ))

  const options = (getEmptyOptionIndex(selectedOptions) !== -1) ? [{label: value, option: false}] : propertyOptions
  return {
    type: CHANGE_TIMESERIES_FILTER_VALUE,
    payload: {input: value, options: fromJS(options)}
  }
}

export const changeTimeSeriesProperty = (key, value) => ({
  type: CHANGE_TIMESERIES_PROPERTY,
  payload: {key, value}
})

export const saveTimeSeries = () => ({
  type: SAVE_TIMESERIES
})

export const addTimeSeries = (agentDescriptions) => ({
  type: ADD_TIMESERIES,
  payload: {agentDescriptions}
})

export const editTimeSeries = (timeSeries) => ({
  type: EDIT_TIMESERIES,
  payload: {timeSeries}
})

export const deleteTimeSeries = (id) => ({
  type: DELETE_TIMESERIES,
  payload: {id}
})

export const rememberSelectedTimeSeriesProperty = (value) => ({
  type: REMEMBER_SELECTED_TIMESERIES_PROPERTY,
  payload: {value}
})

export const resetTimeSeries = () => ({
  type: RESET_TIMESERIES
})
