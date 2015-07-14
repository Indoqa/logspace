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
import Tabs from 'react-simpletabs'

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
      <Tabs>
        <Tabs.Panel title='Export'>
          <div className='options'>
            <b>Export current configuration (selected time window and time series) to a json file:</b>
            <br/>
            <br/>
            <a className='exportButton waves-effect waves-light btn'
              href={'data:text/json;charset=utf-8,' + encodeURIComponent(exportedState)}
              download={this.getDownloadName()}>
              Download file
            </a>
          </div>
        </Tabs.Panel>
        <Tabs.Panel title='Import'>
          <div className='options'>
            <Dropzone onDrop={this.onDrop} size={225} >
              <div className='dropzone'>
                <div className='text'> 
                  Drop a Logspace configuration file or 
                  <br/> 
                  <br/> 
                  <a className='exportButton waves-effect waves-light btn'>Select file</a>
                </div>
              </div>    
            </Dropzone>
          </div>
        </Tabs.Panel>
      </Tabs>
    )
  }
}
