/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

export const UPDATE_EDITABLE_STATE = 'UPDATE_EDITABLE_STATE'

export const updateEditableState = (id, name, state) => ({
  type: UPDATE_EDITABLE_STATE,
  payload: {
    id, name, state
  }
})
