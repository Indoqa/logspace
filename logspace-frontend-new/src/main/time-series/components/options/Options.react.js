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

  componentWillReceiveProps(nextProps) {
    if (nextProps.downloadBlob) {
      this.downloadBlob(nextProps.downloadBlob, 'csv')
      this.props.clearDownload()
    }

    if (nextProps.exportedBlob) {
      this.downloadBlob(nextProps.exportedBlob, 'json')
      this.props.clearExport()
    }
  }

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

  getDownloadName(extension) {
    const {chartTitle} = this.props
    const datePart = moment().format('YYYYDDMM-HHmmss')

    return `${chartTitle}-${datePart}.${extension}`.replace(/\s/g, '_').toLowerCase()
  }

  downloadBlob(blob, extension) {
    const downloadLink = document.createElement('a')
    document.body.appendChild(downloadLink) // Firefox requires the link to be in the body
    downloadLink.href = window.URL.createObjectURL(blob)
    downloadLink.setAttribute('download', this.getDownloadName(extension))
    downloadLink.click()
    document.body.removeChild(downloadLink)
  }

  renderButton(hasTimeSeries, downloadFunction) {
    if (hasTimeSeries) {
      return (
        <a
          className="exportButton waves-effect waves-light btn"
          onClick={() => downloadFunction()}
        >Download file</a>)
    }

    return (<span>No time series selected</span>)
  }

  render() {
    const {requestExport, hasTimeSeries, requestDownload, ...other} = this.props

    const exportButton = this.renderButton(hasTimeSeries, requestExport)
    const downloadButton = this.renderButton(hasTimeSeries, requestDownload)

    return (
      <Tabs>
        <Tabs.Panel title="Import">
          <div className="options">
            <Dropzone {...other} onDrop={this.onDrop} size={225} >
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
            {exportButton}
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
        <Tabs.Panel title="Download">
          <div className="options">
            <b>Download events for the current configuration (selected time window and time series) as a csv file:</b>
            <br />
            <br />
            {downloadButton}
          </div>
        </Tabs.Panel>
      </Tabs>
    )
  }
}

Options.propTypes = {
  chartTitle: PropTypes.string.isRequired,
  clearExport: PropTypes.func.isRequired,
  clearDownload: PropTypes.func.isRequired,
  hasTimeSeries: PropTypes.bool.isRequired,
  refreshResult: PropTypes.func.isRequired,
  requestExport: PropTypes.func.isRequired,
  importState: PropTypes.func.isRequired,
  requestDownload: PropTypes.func.isRequired,
}
