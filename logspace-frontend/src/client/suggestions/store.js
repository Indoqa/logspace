/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import Immutable from 'immutable'
import axios from 'axios'

import {suggestionCursor} from '../state'
import {register} from '../dispatcher'
import {restUrl} from '../environment'

import * as actions from './actions'

export const SuggestionStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case actions.onNewSuggestionQuery:
      updateRequest('text', data)
      refreshSelections()
      break

    case actions.onSystemSelected:
      updateRequest('system', data)
      refreshSelections()
      break

    case actions.onSystemCleared:
      updateRequest('system', null)
      refreshSelections()
      break

    case actions.onSpaceSelected:
      updateRequest('space', data)
      refreshSelections()
      break

    case actions.onSpaceCleared:
      updateRequest('space', null)
      refreshSelections()
      break
    case actions.onPropertySelected:
      updateRequest('property', data)
      refreshSelections()
      break

    case actions.onPropertyCleared:
      updateRequest('property', null)
      refreshSelections()
      break
  }
})

function updateRequest(key, value) {
  suggestionCursor(suggestions => {
    return suggestions.setIn(['request', key], Immutable.fromJS(value))
  })
}

function refreshSelections() {
  var request = suggestionCursor().get('request').toJS()

  suggestionCursor(suggestions => {
    return suggestions.setIn(['result', 'loading'], true)
  })

  var translatedRequest = {
    text: request.text,
    systemId: (request.system)?request.system.id:null,
    spaceId: (request.space)?request.space.id:null,
    propertyId: (request.property)?request.property.id:null,
  }

  axios.post(restUrl('/suggestion'), translatedRequest)
  .then(function (response) {
    storeSuccessResult(response.data)
  })
  .catch(function (response) {
    storeErrorResult(response)
  })
}

function storeErrorResult(serverResponse) {
  suggestionCursor(suggestions => {
    return suggestions.set('result', Immutable.fromJS({
        error: true,
        loading: false,
        spaces: [],
        systems: [],
        propertyNames: [],
        agentDescriptions: []
    }))
  })
}

function storeEmptyResult() {
  suggestionCursor(suggestions => {
    return suggestions.set('result', Immutable.fromJS({
        error: false,
        loading: false,
        spaces: [],
        systems: [],
        propertyNames: [],
        agentDescriptions: []
    }))
  })
}

function storeSuccessResult(responseJson) {
  suggestionCursor(suggestions => {
    return suggestions.set('result', Immutable.fromJS({
        error: false,
        loading: false,
        spaces: responseJson.spaces,
        systems: responseJson.systems,
        propertyNames: responseJson.propertyNames,
        agentDescriptions: responseJson.agentDescriptions
    }))
  })
}
