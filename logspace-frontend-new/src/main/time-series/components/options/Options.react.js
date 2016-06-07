/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import Dropzone from 'react-dropzone'
import moment from 'moment'
import Tabs from 'react-simpletabs'

require('./Options.styl')


export default class Options extends React.Component {

  onDrop(files) {
    const {importState, refreshResult} = this.props
    const reader = new FileReader()

    reader.onloadend = () => {
      try {
        importState(reader.result)
        refreshResult()
      } catch (e) {
        alert(`Error importing file: ${e}`) // eslint-disable-line no-alert
      }
    }

    reader.readAsText(files[0])
  }

  getDownloadName() {
    const {chartTitle} = this.props
    const datePart = moment().format('YYYYDDMM-HHmmss')

    return `${chartTitle}-${datePart} + '.json'`.replace(/\s/g, '_').toLowerCase()
  }

  render() {
    const {getExportState} = this.props

    const exportedState = getExportState()

    return (
      <Tabs>
        <Tabs.Panel title="Import">
          <div className="options">
            <Dropzone onDrop={this.onDrop} size={225} >
              <div className="dropzone">
                <div className="text">
                  Drop a Logspace configuration file or
                  <br />
                  <br />
                  <a className="exportButton waves-effect waves-light btn">Select file</a>
                </div>
              </div>
            </Dropzone>
          </div>
        </Tabs.Panel>
        <Tabs.Panel title="Export">
          <div className="options">
            <b>Export current configuration (selected time window and time series) to a json file:</b>
            <br />
            <br />
            <a
              className="exportButton waves-effect waves-light btn"
              href={`data:text/json;charset=utf-8,${encodeURIComponent(exportedState)}`}
              download={this.getDownloadName()}
            >
              Download file
            </a>
            <br />
            <br />
            <br />
            <br />
            <br />
            <br />
            <br />
            <br />
          </div>
        </Tabs.Panel>
      </Tabs>
    )
  }
}

Options.propTypes = {
  chartTitle: PropTypes.string.isRequired,
  refreshResult: PropTypes.func.isRequired,
  getExportState: PropTypes.func.isRequired,
  importState: PropTypes.func.isRequired
}
