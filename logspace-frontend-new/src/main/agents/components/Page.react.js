/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import AgentActivityEntry from './AgentActivityEntry.react'
import BasePage from '../../app/components/BasePage.react'
import Header from './Header.redux'
import RefreshIcon from '../../app/components/autoplay/RefreshIcon.react'
import RunIcon from '../../app/components/autoplay/RunIcon.react'
import {Button, Dropdown, DropdownMenu, NavItem} from 'rebass'
import {Flex, Box} from 'reflexbox'
import {FontAwesome} from 'indoqa-rebass-components'
import 'font-awesome/css/font-awesome.css'

const BREADCRUMBS = [{children: 'Agents'}]
const UNITS = [
  {value: 604800, name: 'Week'},
  {value: 86400, name: 'Day'},
  {value: 3600, name: 'Hour'},
  {value: 60, name: 'Minute'},
]

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
      <BasePage title="Agents" breadcrumbs={BREADCRUMBS} headerComponents={<Header />}>
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
      </BasePage>
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
