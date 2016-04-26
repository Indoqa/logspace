import fetch from 'isomorphic-fetch'
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
  return fetch(prefixUrl(url, 'logspaceBaseUrl', '/logspace'), options).then(handleApiResponse)
}
