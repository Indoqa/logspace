/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

// see http://davidwalsh.name/javascript-debounce-function
export default function debounceFunc(func, wait, immediate) {
  let timeout

  return () => {
    const context = this
    const args = [func, wait, immediate]

    const later = () => {
      timeout = null
      if (!immediate) func.apply(context, args)
    }

    const callNow = immediate && !timeout
    clearTimeout(timeout)

    timeout = setTimeout(later, wait)
    if (callNow) func.apply(context, args)
  }
}
