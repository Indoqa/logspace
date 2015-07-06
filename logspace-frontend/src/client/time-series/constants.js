/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

export const COLORS = [
  '#e51c23',
  '#5677fc',
  '#f4b400',
  '#259b24',
  '#673ab7',
  '#009688',
  '#e91e63',
  '#00bcd4'
]

export const SCALES = {
  "integer_property_response_code": {
    label: 'HTTP Status codes',
    min: 0,
    max: 500
  }
}

export const TYPES = [
  'bar',
  'line',
  'area'
]

export function isSubitem(scaleType) {
  return scaleType.indexOf('subitem-') == 0
}

export function getReference(scaleType) {
  if (!isSubitem(scaleType)) {
    return null
  }

  return scaleType.substring(8)
}
