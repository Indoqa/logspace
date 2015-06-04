/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import State from '../lib/state'

export const state = new State(require('./initialstate'))
export const i18nCursor = state.cursor(['i18n'])
export const timeWindowCursor = state.cursor(['timeWindow'])
export const timeSeriesCursor = state.cursor(['timeSeries'])
export const editedTimeSeriesCursor = state.cursor(['editedTimeSeries'])
export const resultCursor = state.cursor(['result'])
export const suggestionCursor = state.cursor(['suggestions'])
export const viewCursor = state.cursor(['view'])
