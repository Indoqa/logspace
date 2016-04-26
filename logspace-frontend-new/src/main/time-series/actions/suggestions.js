/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {fetchLogspace} from '../../app/utils/fetchApis'

export const REFRESH_SUGGESTIONS = 'REFRESH_SUGGESTIONS'
export const SHOW_SUGGESTIONS = 'SHOW_SUGGESTIONS'
export const SET_SUGGESTION_QUERY = 'SET_SUGGESTION_QUERY'
export const SELECT_SYSTEM = 'SELECT_SYSTEM'
export const CLEAR_SYSTEM = 'CLEAR_SYSTEM'
export const SELECT_SPACE = 'SELECT_SPACE'
export const CLEAR_SPACE = 'CLEAR_SPACE'
export const SELECT_PROPERTY = 'SELECT_PROPERTY'
export const CLEAR_PROPERTY = 'CLEAR_PROPERTY'

export const refreshSuggestions = (store) => () => {
  const request = store.suggestions.get('request')

  return {
    type: REFRESH_SUGGESTIONS,
    payload: {
      promise: fetchLogspace('/suggestion', {
        method: 'POST',
        body: JSON.stringify({
          text: request.text,
          systemId: (request.system) ? request.system.id : null,
          spaceId: (request.space) ? request.space.id : null,
          propertyId: (request.property) ? request.property.id : null
        })
      })
    }
  }
}

export const showSuggestions = () => ({
  type: SHOW_SUGGESTIONS
})

export const setSuggestionQuery = (query) => ({
  type: SET_SUGGESTION_QUERY,
  payload: {query}
})

export const selectSystem = (system) => ({
  type: SELECT_SYSTEM,
  payload: {system}
})

export const clearSystem = () => ({
  type: CLEAR_SYSTEM
})

export const selectSpace = (space) => ({
  type: SELECT_SPACE,
  payload: {space}
})

export const clearSpace = () => ({
  type: CLEAR_SPACE
})

export const selectProperty = (property) => ({
  type: SELECT_PROPERTY,
  payload: {property}
})

export const clearProperty = () => ({
  type: CLEAR_PROPERTY
})

