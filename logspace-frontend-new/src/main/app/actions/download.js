/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {fetchLogspace} from '../../apis'
import {createRestRequest} from '../../time-series/actions/result'

export const CLEAR_DOWNLOAD = 'CLEAR_DOWNLOAD'
export const REQUEST_DOWNLOAD = 'REQUEST_DOWNLOAD'

export const clearDownload = () => {
  return {
    type: CLEAR_DOWNLOAD
  }
}

export const requestDownload = () => ({store}) => {
  const timeSeries = store.getState().timeSeries.get('timeSeries')
  const timeWindow = store.getState().timeWindow

  return {
    type: REQUEST_DOWNLOAD,
    payload: {
      promise: fetchLogspace('/download', {
        method: 'POST',
        responseType: 'blob',
        body: JSON.stringify(createRestRequest(timeSeries, timeWindow.selection)),
      }).then((response) => response.blob())
    }
  }
}
