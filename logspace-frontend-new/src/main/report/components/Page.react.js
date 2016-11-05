/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import moment from 'moment'

export default class Page extends React.Component {

  componentDidMount() {
    this.props.loadReports()
  }

  formatTimestamp(timestamp) {
    if (!timestamp) {
      return 'UNKNOWN'
    }

    return moment(timestamp).format('MM/DD/YYYY HH:mm:ss')
  }

  formatTimeSeries(timeSeries) {
    if (!timeSeries) {
      return '0'
    }

    return timeSeries.size
  }

  render() {
    if (this.props.loading) {
      return <div>Loading ...</div>
    }

    if (!this.props.reports) {
      return <div>No reports found!</div>
    }

    return (
      <div>
        <h1>Reports</h1>
        <table>
          <thead>
            <tr>
              <td>Name</td>
              <td>Editor</td>
              <td># Time series</td>
              <td>Last modification</td>
            </tr>
          </thead>
          <tbody>
            {this.props.reports.map((report, index) =>
              <tr key={index}>
                <td>{report.get('name')}</td>
                <td></td>
                <td>{this.formatTimeSeries(report.get('timeSeries'))}</td>
                <td>{this.formatTimestamp(report.get('timestamp'))}</td>
              </tr>)
            }
          </tbody>
        </table>
      </div>
    )
  }
}

Page.propTypes = {
  loading: PropTypes.bool.isRequired,
  reports: PropTypes.object,

  loadReports: PropTypes.func.isRequired
}
