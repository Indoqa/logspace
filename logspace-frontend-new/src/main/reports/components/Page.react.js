/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import {Link} from 'react-router'
import moment from 'moment'

import {Div} from 'indoqa-rebass-components'
import BasePage from '../../app/components/BasePage.react'

import {ROUTE_REPORT} from '../../routes'

const BREADCRUMBS = [{children: 'Reports'}]

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

  renderMain() {
    if (this.props.loading) {
      return <Div>Loading ...</Div>
    }

    if (!this.props.reports) {
      return <Div>No reports found!</Div>
    }

    return (
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
              <td><Link to={ROUTE_REPORT.replace(':reportId', report.get('id'))}>{report.get('name')}</Link></td>
              <td></td>
              <td>{this.formatTimeSeries(report.get('timeSeries'))}</td>
              <td>{this.formatTimestamp(report.get('timestamp'))}</td>
            </tr>)
          }
        </tbody>
      </table>
    )
  }

  render() {
    return (
      <BasePage title="Reports" breadcrumbs={BREADCRUMBS}>
        {this.renderMain()}
      </BasePage>
    )
  }
}

Page.propTypes = {
  loading: PropTypes.bool.isRequired,
  reports: PropTypes.object,

  loadReports: PropTypes.func.isRequired
}
