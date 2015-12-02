/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import Immutable from 'immutable'

import {register} from '../dispatcher'

import {agentActivityCursor} from '../state'

import * as actions from './actions'

export const AgentActivityStore_dispatchToken = register(({action, data}) => {
  switch (action) {
    case actions.load:
      load(data)
      break

    case actions.toggleAutoPlay:
      toggleAutoPlay()
      break

    case actions.setDuration:
      setDuration(data)
      break

    case actions.setSort:
      setSort(data)
      break
  }
})

function load(data) {
  agentActivityCursor(cursor => {
    return cursor.set('result', data)
  })
}

function setDuration(duration) {
  agentActivityCursor(cursor => {
    return cursor.set('duration', duration)
  })
}

function setSort(sort) {
  agentActivityCursor(cursor => {
    return cursor.set('sort', sort)
  })
}

function toggleAutoPlay() {
  agentActivityCursor(cursor => {
    var autoPlay = !cursor.get('autoPlay')
    return cursor.set('autoPlay', autoPlay)
  })
}
