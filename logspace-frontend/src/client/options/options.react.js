/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import Component from '../components/component.react'
import Dropzone from 'react-dropzone'
import moment from 'moment'

import {getExportState, importState} from '../state'
import {refreshResult} from '../result/actions'


require('./options.styl')


export default class Options extends Component {

  onDrop (files) {
    var reader = new FileReader()

    reader.onloadend = function () {
      try {
        importState(reader.result)
        refreshResult()
      } catch(e) {
        alert('Error importing file: ' + e)
      }
    }

    reader.readAsText(files[0])
  }

  getDownloadName() {
    const datePart = moment().format('-YYYYDDMM-HHmmss')
    return new String(this.props.chartTitle + datePart + '.json').replace(/\s/g, "_").toLowerCase()
  }

  render() {
    const exportedState = getExportState()

    return (
      <div className='options'>
        <b>Export current selection (time window and time series)</b>
        <br/>
        <br/>
        <a className='exportButton waves-effect waves-light btn'
          href={'data:text/json;charset=utf-8,' + encodeURIComponent(exportedState)}
          download={this.getDownloadName()}>
          Export
        </a>
        <br/>
        <br/>
        <b>Import selection</b>
        <br/>
        <br/>
        <Dropzone onDrop={this.onDrop} size={125} >
          <div className='dropzone'>Drop a Logspace configuration file or click to select it from your file system.</div>
        </Dropzone>
      </div>
    )
  }
}
