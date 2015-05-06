/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import Immutable from 'immutable';
import axios from 'axios';
import * as actions from './actions';
import {suggestionCursor} from '../state';
import {register} from '../dispatcher';
import {getRestUrl} from '../rest';

export function getSuggestions() {
  return suggestionCursor();
}

export const SuggestionStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case actions.onNewSuggestionQuery:
      refreshSelections(data); 
    break;  
  }
});

function refreshSelections(query) {
  if (query == null || query.length == 0) {
    storeEmptyResult()  
  }

  axios.get(getRestUrl('/suggest/') + query)
  .then(function (response) {
    storeSuccessResult(response.data)  
  })
  .catch(function (response) {
    storeErrorResult(response)  
  });
}  

function storeErrorResult(serverResponse) {
  suggestionCursor(result => {
    return suggestionCursor.set("error", true);
  });
}

function storeEmptyResult() {
  suggestionCursor(suggestions => {
    return suggestions.set("spaces", [])
  });

  suggestionCursor(suggestions => {
    return suggestions.set("agentIds", [])
  });

  suggestionCursor(suggestions => {
    return suggestions.set("propertyNames", [])
  });
}

function storeSuccessResult(responseJson) {
  suggestionCursor(suggestions => {
    return suggestions.set("spaces", Immutable.fromJS(responseJson.spaces))
  });

  suggestionCursor(suggestions => {
    return suggestions.set("agentIds", Immutable.fromJS(responseJson.agentIds))
  });

  suggestionCursor(suggestions => {
    return suggestions.set("propertyNames", Immutable.fromJS(responseJson.propertyNames))
  });
}


