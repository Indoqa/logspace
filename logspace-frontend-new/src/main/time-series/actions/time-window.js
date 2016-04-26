/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

export const RESET_TIMEWINDOW = 'RESET_TIMEWINDOW'
export const SHOW_TIMEWINDOW_FORM = 'SHOW_TIMEWINDOW_FORM'
export const SHOW_TIMEWINDOW_TAB = 'SHOW_TIMEWINDOW_TAB'
export const SELECT_CUSTOM_DATE = 'SELECT_CUSTOM_DATE'
export const SELECT_DYNAMIC_DATE = 'SELECT_DYNAMIC_DATE'
export const SELECT_PREDEFINED_DATE = 'SELECT_PREDEFINED_DATE'

export const resetTimewindow = () => ({
  type: RESET_TIMEWINDOW
})

export const showTimewindowForm = () => ({
  type: SHOW_TIMEWINDOW_FORM
})

export const selectCustomDate = (start, end, gap) => ({
  type: SELECT_CUSTOM_DATE,
  payload: {start, end, gap}
})


export const selectDynamicDate = (duration, unit, gap) => ({
  type: SELECT_DYNAMIC_DATE,
  payload: {duration, unit, gap}
})

export const selectPredefinedDate = (selection) => ({
  type: SELECT_PREDEFINED_DATE,
  payload: {selection}
})

export const showTimewindowTab = (index) => ({
  type: SHOW_TIMEWINDOW_TAB,
  payload: {index}
})
