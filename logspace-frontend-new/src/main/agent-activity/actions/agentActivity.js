/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {fetchLogspace} from '../../app/utils/fetchApis'

const ACTION_PREFIX = 'AGENT_ACTIVITY|'

export const SET_DURATION = `${ACTION_PREFIX}SET_DURATION`
export const SET_SORT = `${ACTION_PREFIX}SET_SORT`
export const START_AUTOPLAY = `${ACTION_PREFIX}START_AUTOPLAY`
export const STOP_AUTOPLAY = `${ACTION_PREFIX}STOP_AUTOPLAY`
export const RESET_AUTOPLAY_COUNTDOWN = `${ACTION_PREFIX}RESET_AUTOPLAY_COUNTDOWN`
export const DECREMENT_AUTOPLAY_COUNTDOWN = `${ACTION_PREFIX}DECREMENT_AUTOPLAY_COUNTDOWN`
export const LOAD_AGENT_ACTIVITIES = `${ACTION_PREFIX}LOAD_AGENT_ACTIVITIES`

const updateDuration = (duration) => (
  {
    type: SET_DURATION,
    payload: duration,
  }
)

const updateSort = (sort) => (
  {
    type: SET_SORT,
    payload: sort,
  }
)

export const loadAgentActivities = () => ({getState}) => {
  const duration = getState().agentActivity.get('duration')
  const sort = getState().agentActivity.get('sort')

  return {
    type: LOAD_AGENT_ACTIVITIES,
    payload: fetchLogspace(`/agent-activity?steps=60&duration=${duration}&sort=${sort}`)
  }
}

const decrementAutoPlayCountdown = () => ({getState, dispatch}) => {
  dispatch({type: DECREMENT_AUTOPLAY_COUNTDOWN})

  const countdown = getState().agentActivity.getIn(['autoPlay', 'countdown'])

  if (countdown === 0) {
    dispatch({type: RESET_AUTOPLAY_COUNTDOWN})
    dispatch(loadAgentActivities())
  }
}

const startAutoPlay = () => ({getState, dispatch}) => {
  dispatch({type: START_AUTOPLAY})

  const intervalId = setInterval(() => {
    const isRunning = getState().agentActivity.getIn(['autoPlay', 'running'])

    if (isRunning) {
      dispatch(decrementAutoPlayCountdown())
    } else {
      clearInterval(intervalId)
    }


  }, 1000)
}

const stopAutoPlay = () => ({dispatch}) => {
  dispatch({type: STOP_AUTOPLAY})
}

export const toggleAutoPlay = () => ({getState, dispatch}) => {
  const isRunning = getState().agentActivity.getIn(['autoPlay', 'running'])

  if (!isRunning) {
    dispatch(startAutoPlay())
  } else {
    dispatch(stopAutoPlay())
  }
}

export const setDuration = (duration) => (
  [
    updateDuration(duration),
    loadAgentActivities(),
  ]
)

export const setSort = (sort) => (
  [
    updateSort(sort),
    loadAgentActivities(),
  ]
)
