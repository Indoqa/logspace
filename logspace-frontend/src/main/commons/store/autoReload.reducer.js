// @flow

import * as R from 'ramda'

const START_COUNTDOWN = R.always(10)
const TRUE = R.T
const FALSE = R.F

const createTransformation = (autoReloadPath: any, modification: any) => {
  if (autoReloadPath instanceof Array) {
    return R.assocPath(autoReloadPath, modification, {})
  }

  return R.assoc(autoReloadPath, modification, {})
}

export const createUpdateCountdown = (autoReloadPath: any) => {
  const transformation = createTransformation(autoReloadPath, {countdown: R.dec})
  return (state: any) => R.evolve(transformation, state)
}

export const createOnResult = (autoReloadPath: any) => {
  const transformation = createTransformation(autoReloadPath, {countdown: START_COUNTDOWN, isLoading: FALSE})
  return (state: any) => R.evolve(transformation, state)
}

export const createOnError = (autoReloadPath: any) => {
  const transformation = createTransformation(autoReloadPath, {isLoading: FALSE})
  return (state: any) => R.evolve(transformation, state)
}

export const createStart = (autoReloadPath: any) => {
  const transformation = createTransformation(autoReloadPath, {countdown: START_COUNTDOWN, active: TRUE})
  return (state: any) => R.evolve(transformation, state)
}

export const createStop = (autoReloadPath: any) => {
  const transformation = createTransformation(autoReloadPath, {active: FALSE})
  return (state: any) => R.evolve(transformation, state)
}

export const createOnStartLoading = (autoReloadPath: any) => {
  const transformation = createTransformation(autoReloadPath, {isLoading: TRUE})
  return (state: any) => R.evolve(transformation, state)
}
