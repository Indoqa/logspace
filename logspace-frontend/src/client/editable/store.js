/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import Immutable from 'immutable'
import {register} from '../dispatcher'

import * as actions from './actions'
import {viewCursor} from '../state'

export const EditableStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case actions.onEditableState:
    viewCursor(view => {
      const {id, name, state} = data;
      return view.setIn(['editables', id, name], state);
  });
  }
})

