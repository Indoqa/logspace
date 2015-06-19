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
        const loadedState = JSON.parse(reader.result)
        importState(loadedState)
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
    const exportedState = JSON.stringify(getExportState())

    return (
      <div className='options'>
        <b>Export to file</b>
        <br/>
        <br/>
        <button className='exportButton waves-effect waves-light btn'>
          <a
            href={'data:text/json;charset=utf8,' + encodeURIComponent(exportedState)}
            download={this.getDownloadName()}
            target="_blank">
            Export
          </a>
        </button>
        <br/>
        <br/>
        <b>Import file</b>
        <br/>
        <br/>
        <Dropzone onDrop={this.onDrop} size={125} >
          <div className='dropzone'>Drop a Logspace configuration file or click to select it from your file system.</div>
        </Dropzone>
      </div>
    )
  }
}
