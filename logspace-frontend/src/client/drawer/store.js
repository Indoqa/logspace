/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import Immutable from 'immutable';
import * as timeWindowActions from '../time-window/actions';
import * as timeSeriesActions from '../time-series/actions';
import * as suggestionsActions from '../suggestions/actions';
import * as drawerActions from './actions';
import * as Panels from './constants';
import {drawerCursor} from '../state';
import {register,waitFor} from '../dispatcher';

export function getActivePanel() {
  return drawerCursor().get("activePanel");
}

export const DrawerStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case suggestionsActions.onShowSuggestions:
      setActivePanel(Panels.SUGGESTIONS);
      break;
    
    case timeWindowActions.onShowTimeWindowForm:
      setActivePanel(Panels.TIME_WINDOW);
      break;  

    case timeWindowActions.onTimeWindowChange:
      setActivePanel(null);
      break;
      
    case drawerActions.onCloseDrawer:
      setActivePanel(null);
      break;

    case timeSeriesActions.onTimeSeriesAdded:
      setActivePanel(null);  
      break;
  }
});  

function setActivePanel(panel) {
  drawerCursor(result => {
    return result.set("activePanel", panel)
  }); 
}


