/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import State from '../lib/state'
import moment from 'moment'
import {shortcutById} from './time-window/constants'

const VERSION = 2

export const state = new State(require('./initialstate'))
export const i18nCursor = state.cursor(['i18n'])
export const timeWindowCursor = state.cursor(['timeWindow'])
export const timeSeriesCursor = state.cursor(['timeSeries'])
export const timeSeriesDefaultsCursor = state.cursor(['timeSeriesDefaults'])
export const editedTimeSeriesCursor = state.cursor(['editedTimeSeries'])
export const resultCursor = state.cursor(['result'])
export const suggestionCursor = state.cursor(['suggestions'])
export const viewCursor = state.cursor(['view'])


export function getExportState() {
	const timeWindow = timeWindowCursor().toJS()

	return JSON.stringify({
		version: VERSION,
		timeWindow: timeWindow,
		timeSeries: timeSeriesCursor().toJS(),
		serializedTimeWindowRange: serializeTimeWindowRange(timeWindow)
	})
}

export function importState(importedStateAsString) {
	const importedState = JSON.parse(importedStateAsString)

	if (importedState.version < VERSION) {
		throw "Version " + importedState.version + " not supported!"
	}

	deserializeTimeWindowRange(importedState)

	console.log('import state:', importedState)

	const mergedState = state.get().merge(importedState)
	state.set(mergedState)
}

function serializeTimeWindowRange(timeWindow) {	
	switch (timeWindow.selection.type) {
		case 'dynamic':
			return {
				type: 'dynamic',
				duration: timeWindow.dynamic.range.amount,
				unit: timeWindow.dynamic.range.unit
			}

		case 'shortcut':
			return {
				type: 'shortcut',
				shortcutId: timeWindow.selection.shortcutId
			}
			
		case 'custom':
			return {
				type: 'custom',
				start: timeWindow.selection.start().toISOString(),
				end: timeWindow.selection.end().toISOString()
			} 
	}

  return {}
}

function deserializeTimeWindowRange(importedState) {	
	const serializedTimeWindowRange = importedState.serializedTimeWindowRange

	if (!importedState.serializedTimeWindowRange) {
		return
	}

	switch (serializedTimeWindowRange.type) {
		case 'dynamic':
			importedState.timeWindow.selection.start =  () => moment().subtract(serializedTimeWindowRange.duration, serializedTimeWindowRange.unit.label)
      importedState.timeWindow.selection.end = () => moment()
			break

		case 'shortcut':
			const shortcut = shortcutById(serializedTimeWindowRange.shortcutId)
			
			if (!shortcut) {
				return
			}

			importedState.timeWindow.selection.start =  () => shortcut.start()
      importedState.timeWindow.selection.end = () => shortcut.end()

			break
			
		case 'custom':
		  importedState.timeWindow.selection.start =  () => moment(serializedTimeWindowRange.start)
      importedState.timeWindow.selection.end = () => moment(serializedTimeWindowRange.end)
			break
	}
}

