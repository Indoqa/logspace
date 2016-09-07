/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import handleApiResponse from './handleApiResponse'

const prefixUrl = (url, property, defaultPrefix) => {
  const environmentProperty = window[property]

  if (!environmentProperty) {
    return `${defaultPrefix}${url}`
  }

  if (typeof environmentProperty === 'function') {
    return environmentProperty(url)
  }

  return `${environmentProperty}${url}`
}

export const fetchLogspace = (url, options) => {
  const defaultPrefix = process.env.NODE_ENV !== 'production' ? '/logspace' : ''
  return fetch(prefixUrl(url, 'logspaceBaseUrl', defaultPrefix), options).then(handleApiResponse)
}
