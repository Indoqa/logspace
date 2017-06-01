
// @flow

import {Observable} from 'rxjs'

import type {AutoReload} from '../types/AutoReload'


export const createStartAutoReloadEpic = (startEventType: string, endEventType: string, updateActionCreator: Function) => {
  return (action$: any) => action$
    .ofType(startEventType)
    .switchMap(() => Observable
      .interval(1000)
      .map(updateActionCreator)
      .takeUntil(action$.ofType(endEventType)))
}

const isCountdownZero = (state: any, selectAutoReload: Function) => {
  const autoReload: AutoReload = selectAutoReload(state)
  return autoReload.countdown === 0
}

export const createOnCountdownUpdatedEpic = (updateEventType: string, selectAutoReload: Function, loadActionCreator: Function) => {
  return (action$: any, store: any) => action$
    .ofType(updateEventType)
    .filter(() => isCountdownZero(store.getState(), selectAutoReload))
    .map(loadActionCreator)
}
