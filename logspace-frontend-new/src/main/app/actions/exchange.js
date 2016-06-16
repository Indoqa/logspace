/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import moment from 'moment'
import {shortcutById} from '../../time-series/actions/timeWindow.constants.js'

const VERSION = 2
export const EXPORT_STATE = 'EXPORT_STATE'
export const IMPORT_STATE = 'IMPORT_STATE'
export const RESET_STATE = 'RESET_STATE'

export const EXPORT_BLOB = 'EXPORT_BLOB'
export const CLEAR_EXPORT_BLOB = 'CLEAR_EXPORT_BLOB'

const deserializeTimeWindowRange = (importedState) => {
  const timeWindow = importedState.serializedTimeWindowRange

  if (!timeWindow) {
    return
  }

  switch (timeWindow.type) {
    case 'dynamic':
      importedState.timeWindow.selection.start = () => moment().subtract(timeWindow.duration, timeWindow.unit.label)
      importedState.timeWindow.selection.end = () => moment()
      break

    case 'custom':
      importedState.timeWindow.selection.start = () => moment(timeWindow.start)
      importedState.timeWindow.selection.end = () => moment(timeWindow.end)
      break

    case 'shortcut': // eslint-disable-line no-case-declarations
      let shortcut = shortcutById(timeWindow.shortcutId)

      if (!shortcut) {
        return
      }
      shortcut = shortcut.toJS()

      importedState.timeWindow.selection.start = () => shortcut.start()
      importedState.timeWindow.selection.end = () => shortcut.end()
      break

    default:
      break
  }
}

const serializeTimeWindowRange = (timeWindow) => {
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

    default:
      return {}
  }
}

const serializeState = (timeWindow, timeSeries) => {
  return JSON.stringify(
    {version: VERSION,
     timeWindow,
     timeSeries,
     serializedTimeWindowRange: serializeTimeWindowRange(timeWindow)
   })
}

export const resetState = () => ({
  type: RESET_STATE
})

export const importState = (serializedState) => {
  const importedState = JSON.parse(serializedState)

  if (importedState.version < VERSION) {
    throw Error(`Version ${importedState.version} not supported!`)
  }

  deserializeTimeWindowRange(importedState)

  return {
    type: IMPORT_STATE,
    payload: {
      importedState
    }
  }
}

export const exportState = () => ({store}) => {
  const timeSeries = store.getState().timeSeries.get('timeSeries')
  const timeWindow = store.getState().timeWindow

  return {
    type: EXPORT_STATE,
    payload: {
      serializedState: serializeState(timeWindow, timeSeries)
    }
  }
}

export const clearExport = () => {
  return {
    type: CLEAR_EXPORT_BLOB
  }
}

export const requestExport = () => ({store}) => {
  const serializedState = store.getState().exchange.get('serializedState')
  return {
    type: EXPORT_BLOB,
    payload: {
      exportBlob: new Blob([serializedState], {type: 'application/json;charset=utf-8'})
    }
  }
}
