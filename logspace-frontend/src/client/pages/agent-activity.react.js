/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
 import React from 'react'
 import {Link} from 'react-router'
 import classnames from 'classnames'
 import Component from '../components/component.react'
 import * as agentActivityActions from '../agent-activity/actions'
 import AgentActivityEntry from '../agent-activity/agent-activity-entry.react'
 import {default as LiteDropdown} from 'react-lite-dropdown';
 import 'react-lite-dropdown/src/style.css';

 export default class AgentActivity extends Component {

   constructor(props) {
     super(props);
   }

   componentDidMount() {
     this.refresh()
   }

   onChange(event) {
     agentActivityActions.setDuration(event.target.value)
   }

   refresh() {
     agentActivityActions.load()
   }

   toggleSort(sortField) {
     var sortAsc = sortField + ' asc'
     var sortDesc = sortField + ' desc'

     var oldSort = this.props.agentActivity.get('sort')
     if (oldSort === sortAsc) {
       agentActivityActions.setSort(sortDesc)
     } else {
       agentActivityActions.setSort(sortAsc)
     }
   }

  render() {
    const duration = this.props.agentActivity.get('duration')
    const maxHistoryValue = this.props.agentActivity.getIn(['result', 'maxHistoryValue'])

    const agentActivities = this.props.agentActivity.getIn(['result', 'agentActivities'])
    const agentActivitiesList = agentActivities.map(agentActivity => <AgentActivityEntry agentActivity={agentActivity} maxHistoryValue={maxHistoryValue} />)

    const durationDropDown = <select onChange={this.onChange} value={duration}>
      <option value="300">5 Minutes</option>
      <option value="900">15 Minutes</option>
      <option value="1800">30 Minutes</option>
      <option value="3600">1 Hour</option>
      <option value="7200">2 Hours</option>
      <option value="21600">6 Hours</option>
      <option value="43200">12 Hours</option>
      <option value="86400">1 Day</option>
      <option value="604800">1 Week</option>
      <option value="2419200">4 Weeks</option>
    </select>

    const isAutoPlay = this.props.agentActivity.get('autoPlay')

    const buttonAutoPlay = <button onClick={agentActivityActions.toggleAutoPlay}>{isAutoPlay ? 'Stop' : 'AutoPlay'}</button>
    const buttonRefresh = <button onClick={() => this.refresh()}>Refresh</button>

    return (
      <div>
        {buttonAutoPlay}&nbsp;{buttonRefresh}<br/>
        {durationDropDown}<br/>
        <table>
          <tr>
            <th onClick={() => this.toggleSort('index')}>Global Agent ID</th>
            <th onClick={() => this.toggleSort('count')}>Event Count</th>
            <th>History</th>
          </tr>
          <tbody>
            {agentActivitiesList}
          </tbody>
        </table>
      </div>
    )
  }
}
