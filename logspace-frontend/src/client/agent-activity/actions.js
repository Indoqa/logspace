/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import Immutable from 'immutable'

import {dispatch} from '../dispatcher';
import {agentActivityCursor} from '../state'
import setToString from '../../lib/settostring';
import debounce from '../../lib/debounce'

import axios from 'axios'

const maybeAutoReplay = debounce(() => {
  var autoPlay = agentActivityCursor().get('autoPlay')
  if (autoPlay) {
    load()
  }
}, 10000)

export function load() {
  const duration = agentActivityCursor().get('duration')
  const sort = agentActivityCursor().get('sort')

  axios.get('/agent-activity?steps=60&duration=' + duration + '&sort=' + sort)
    .then((response) => {
      dispatch(load, Immutable.fromJS(response.data))

      maybeAutoReplay()
    })
}

export function setDuration(duration) {
  dispatch(setDuration, duration)
  load()
}

export function setSort(sort) {
  dispatch(setSort, sort)
  load()
}

export function toggleAutoPlay() {
  dispatch(toggleAutoPlay)

  var autoPlay = agentActivityCursor().get('autoPlay')
  if (autoPlay) {
    load()
  }
}

setToString('agentActivity', {
  load, setDuration, setSort, toggleAutoPlay
});
