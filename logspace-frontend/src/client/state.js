/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import State from '../lib/state'
import moment from 'moment'
import hash from 'hash-code'

import {shortcutById} from './time-window/constants'
import {refreshResult} from './result/actions'

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

export function onApplicationInitialized() {
	if(typeof(Storage) === "undefined") {
	    return
	} 

	loadState(window.location.href)

	state.addListener('change', onSaveStateChange)
	window.addEventListener("hashchange", (e) => loadState(e.newURL))
}

export function getExportState() {
	return getExportState_(timeWindowCursor().toJS(), timeSeriesCursor().toJS())
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

function getExportState_(timeWindow, timeSeries) {
	return JSON.stringify({
		version: VERSION,
		timeWindow: timeWindow,
		timeSeries: timeSeries,
		serializedTimeWindowRange: serializeTimeWindowRange(timeWindow)
	})
}

function onSaveStateChange(state, previousState, path) {
	if(typeof(Storage) === "undefined") {
	    return
	} 

	// TODO: find a better place to store this information
	if (!path || (path[0] != 'timeWindow' && path[0] != 'timeSeries') ) {
		return
	}

	saveStateChange(state)
}

function saveStateChange(state) {
	const stateToExport = state.toJS()
	const exportedStateAsString = getExportState_(stateToExport.timeWindow, stateToExport.timeSeries)
	const key = hash.hashCode(exportedStateAsString);
	
	sessionStorage.setItem("logspaceHistory_" + key, exportedStateAsString)
	location.hash = '/?h=' + key
}

function loadState(url) {
	console.log('loading state from url',url)

	const pos = url.lastIndexOf('?h=');

	if (pos == -1) {
		const initialstate = require('./initialstate')

		state.set(state.get().merge({
			timeWindow: initialstate.timeWindow,
			timeSeries: initialstate.timeSeries
		}))

		refreshResult()
		return
	}

	const hash = url.substring(pos + 3);
  const savedState = sessionStorage.getItem("logspaceHistory_" + hash)

	if (savedState) {
		importState(savedState)
		refreshResult()
	}
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

