/*
* Logspace
* Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
* This program and the accompanying materials are made available under the terms of
* the Eclipse Public License Version 1.0, which accompanies this distribution and
* is available at http://www.eclipse.org/legal/epl-v10.html.
*/
import hashCode from 'hash-code'

import {importState, resetState} from './exchange'
import {refreshResult} from '../../time-series/actions/result'

export const HASH_CHANGED = 'HASH_CHANGED'

const HASH_PARAMTER = 'h='

const createStorageKey = (hash) => {
  return `logspaceHistory_${hash}`
}

const extractHash = (url) => {
  const hashIndex = url.lastIndexOf(HASH_PARAMTER)

  if (hashIndex === -1) {
    return ''
  }

  const hashUrl = url.substring(hashIndex + HASH_PARAMTER.length)

  const nextParamIndex = hashUrl.lastIndexOf('&')

  if (nextParamIndex === -1) {
    return hashUrl
  }

  return hashUrl.substring(0, nextParamIndex)
}

const hashChanged = (hash) => ({
  type: HASH_CHANGED,
  payload: {
    hash
  }
})

export const saveStateChange = () => ({store}) => {
  if (typeof(Storage) === 'undefined') {
    return {type: null}
  }
  const exportedState = store.getState().exchange.get('serializedState')
  const hash = hashCode.hashCode(exportedState)

  sessionStorage.setItem(createStorageKey(hash), exportedState)
  location.hash = `/?${HASH_PARAMTER}${hash}`

  return hashChanged(hash)
}

const loadState = (store, url) => {
  const hash = extractHash(url)

  if (hash === '') {
    store.dispatch(resetState())
    store.dispatch(hashChanged(hash))
    return hashChanged(hash)
  }
  const lastHash = store.getState().history.get('lastHash')

  if (hash == lastHash) { // eslint-disable-line eqeqeq
    return {type: null}
  }

  const savedState = sessionStorage.getItem(createStorageKey(hash))
  if (!savedState) {
    store.dispatch(resetState())
    return {type: null}
  }
  store.dispatch(importState(savedState))
  store.dispatch(refreshResult())
  store.dispatch(hashChanged(hash))

  return hashChanged(hash)
}


export const onApplicationInitialized = () => ({store}) => {
  if (typeof(Storage) === 'undefined') {
    return {type: null}
  }

  window.addEventListener('hashchange', (event) => loadState(store, event.newURL))
  return loadState(store, window.location.href)
}
