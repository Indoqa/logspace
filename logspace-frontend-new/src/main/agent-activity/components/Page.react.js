/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import AgentActivityEntry from './AgentActivityEntry.react'
import Header from './Header.redux'
// import './Page.styl'

export default class Page extends React.Component {

  componentDidMount() {
    this.props.loadAgentActivities()
  }

  toggleSort(sortField) {
    const sortAsc = `${sortField} asc`
    const sortDesc = `${sortField} desc`

    if (this.props.sort === sortAsc) {
      this.props.setSort(sortDesc)
    } else {
      this.props.setSort(sortAsc)
    }
  }

  renderAgentActivity(agentActivity, index) {
    return (<AgentActivityEntry
      agentActivity={agentActivity}
      maxHistoryValue={this.props.maxHistoryValue}
      loading={this.props.loading}
      index={`chart-${index}`}
      key={index}
    />)
  }

  render() {
    const {agentActivities} = this.props

    return (
      <div>
        <Header />
        <table>
          <thead>
            <tr>
              <th onClick={() => this.toggleSort('index')}>Global Agent ID</th>
              <th onClick={() => this.toggleSort('count')}>Event Count</th>
              <th>History</th>
            </tr>
          </thead>
          <tbody>
            {agentActivities.map((eachAgentActivity, index) => this.renderAgentActivity(eachAgentActivity, index)).toList()}
          </tbody>
        </table>
      </div>
    )
  }
}

Page.propTypes = {
  agentActivities: React.PropTypes.object.isRequired,
  maxHistoryValue: React.PropTypes.number.isRequired,
  sort: React.PropTypes.string.isRequired,
  loading: React.PropTypes.bool.isRequired,

  loadAgentActivities: React.PropTypes.func.isRequired,
  setSort: React.PropTypes.func.isRequired,
}
