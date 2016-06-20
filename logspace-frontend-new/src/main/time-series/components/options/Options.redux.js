/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import {connect} from 'react-redux'
import Options from './Options.react'

import {refreshResult} from '../../actions/result'
import {clearExport, requestExport, importState} from '../../../app/actions/exchange'
import {clearDownload, requestDownload} from '../../../app/actions/download'

const mapStateToProps = (state) => ({
  chartTitle: state.result.get('chartTitle'),
  exportedBlob: state.exchange.get('exportBlob'),
  downloadBlob: state.download.get('downloadBlob'),
  hasTimeSeries: state.timeSeries.get('timeSeries').size !== 0
})

const mapDispatchToProps = (dispatch) => ({
  clearExport: () => {
    dispatch(clearExport())
  },
  clearDownload: () => {
    dispatch(clearDownload())
  },
  importState: (serializedState) => {
    dispatch(importState(serializedState))
  },
  refreshResult: () => {
    dispatch(refreshResult())
  },
  requestExport: () => {
    dispatch(requestExport())
  },
  requestDownload: () => {
    dispatch(requestDownload())
  }
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Options)
