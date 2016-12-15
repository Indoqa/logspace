/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {fetchLogspace} from '../../apis'

const ACTION_PREFIX = 'AGENT_ACTIVITY|'

export const SET_DURATION = `${ACTION_PREFIX}SET_DURATION`
export const SET_SORT = `${ACTION_PREFIX}SET_SORT`
export const START_AUTOPLAY = `${ACTION_PREFIX}START_AUTOPLAY`
export const STOP_AUTOPLAY = `${ACTION_PREFIX}STOP_AUTOPLAY`
export const RESET_AUTOPLAY_COUNTDOWN = `${ACTION_PREFIX}RESET_AUTOPLAY_COUNTDOWN`
export const DECREMENT_AUTOPLAY_COUNTDOWN = `${ACTION_PREFIX}DECREMENT_AUTOPLAY_COUNTDOWN`
export const LOAD_AGENT_ACTIVITIES = `${ACTION_PREFIX}LOAD_AGENT_ACTIVITIES`

const updateDuration = (duration) => ({
  type: SET_DURATION,
  payload: duration,
})

const updateSort = (sort) => ({
  type: SET_SORT,
  payload: sort,
})

export const loadAgentActivities = () => ({getState}) => {
  const duration = getState().agents.get('duration')
  const sort = getState().agents.get('sort')

  return {
    type: LOAD_AGENT_ACTIVITIES,
    payload: fetchLogspace(`/agent-activity?steps=60&duration=${duration}&sort=${sort}`)
  }
}

const decrementAutoPlayCountdown = () => ({getState, dispatch}) => {
  dispatch({type: DECREMENT_AUTOPLAY_COUNTDOWN})

  const countdown = getState().agents.getIn(['autoPlay', 'countdown'])
  if (countdown === 0) {
    dispatch(loadAgentActivities())
    dispatch({type: RESET_AUTOPLAY_COUNTDOWN})
  }
}

const startAutoPlay = () => ({getState, dispatch}) => {
  dispatch({type: START_AUTOPLAY})
  dispatch(loadAgentActivities())

  const intervalId = setInterval(() => {
    const isRunning = getState().agents.getIn(['autoPlay', 'running'])
    const isLoading = getState().agents.get('loading')

    if (!isRunning) {
      clearInterval(intervalId)
      return
    }

    if (!isLoading) {
      dispatch(decrementAutoPlayCountdown())
    }
  }, 1000)
}

export const stopAutoPlay = () => ({dispatch}) => {
  dispatch({type: STOP_AUTOPLAY})
}

export const toggleAutoPlay = () => ({getState, dispatch}) => {
  const isRunning = getState().agents.getIn(['autoPlay', 'running'])

  if (!isRunning) {
    dispatch(startAutoPlay())
  } else {
    dispatch(stopAutoPlay())
  }
}

export const setDuration = (duration) => ({dispatch}) => {
  dispatch(updateDuration(duration))
  dispatch(loadAgentActivities())
}

export const setSort = (sort) => ({dispatch}) => {
  dispatch(updateSort(sort))
  dispatch(loadAgentActivities())
}
