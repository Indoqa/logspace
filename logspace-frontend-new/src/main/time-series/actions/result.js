/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

export const REFRESH_RESULT = 'REFRESH_RESULT'
export const SAVE_CHART_TITLE = 'SAVE_CHART_TITLE'
export const SET_CHART_TYPE = 'SET_CHART_TYPE'
export const SET_AUTOPLAY = 'SET_AUTOPLAY'

export const refreshResult = () => ({
  type: REFRESH_RESULT
})

export const saveChartTitle = (title) => ({
  type: SAVE_CHART_TITLE,
  payload: {title}
})

export const setChartType = (type) => ({
  type: SET_CHART_TYPE,
  payload: {type}
})

export const setAutoPlay = (enabled) => ({
  type: SET_AUTOPLAY,
  payload: {enabled}
})
