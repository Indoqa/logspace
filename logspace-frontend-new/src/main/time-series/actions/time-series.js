/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

export const CHANGE_TIMESERIES_PROPERTY = 'CHANGE_TIMESERIES_PROPERTY'
export const SAVE_TIMESERIES = 'SAVE_TIMESERIES'
export const ADD_TIMESERIES = 'ADD_TIMESERIES'
export const EDIT_TIMESERIES = 'EDIT_TIMESERIES'
export const DELETE_TIMESERIES = 'DELETE_TIMESERIES'
export const REMEMBER_SELECTED_TIMESERIES_PROPERTY = 'REMEMBER_SELECTED_TIMESERIES_PROPERTY'
export const RESET_TIMESERIES = 'RESET_TIMESERIES'

export const changeTimeseriesProperty = (key, value) => ({
  type: CHANGE_TIMESERIES_PROPERTY,
  payload: {key, value}
})

export const saveTimeseries = () => ({
  type: SAVE_TIMESERIES
})

export const addTimeseries = (agentDescriptions) => ({
  type: ADD_TIMESERIES,
  payload: {agentDescriptions}
})

export const editTimeseries = (timeSeries) => ({
  type: EDIT_TIMESERIES,
  payload: {timeSeries}
})

export const deleteTimeseries = (id) => ({
  type: DELETE_TIMESERIES,
  payload: {id}
})

export const rememberSelectedTimeseriesProperty = (value) => ({
  type: REMEMBER_SELECTED_TIMESERIES_PROPERTY,
  payload: {value}
})

export const resetTimeseries = () => ({
  type: RESET_TIMESERIES
})
