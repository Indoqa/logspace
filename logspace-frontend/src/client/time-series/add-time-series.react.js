/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
/*
* Logspace
* Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
* This program and the accompanying materials are made available under the terms of
* the Eclipse Public License Version 1.0, which accompanies this distribution and
* is available at http://www.eclipse.org/legal/epl-v10.html.
*/

import React from 'react';
import PureComponent from '../components/purecomponent.react';
import shallowEqual from 'react/lib/shallowEqual';
import {onTimeSeriesAdded} from './actions';

export default class AddTimeSerie extends PureComponent {
  constructor(props) {
    super(props);

    var agent = props.editedTimeSeries.get("agentDescription").toJS();

    this.state = {
      agentId: agent.globalId, 
      propertyId: agent.propertyDescriptions[0].id, 
      aggregate: "sum"
    };
  }

  addTimeSeries() {
    onTimeSeriesAdded(this.state);
    this.props.onSuccess();
  }

  handleChange(event) {
    var newState = {};
    newState[event.target.name] = event.target.value;
    this.setState(newState);
  }

  render() {
    return (
      <div>
        <span> {this.state.agentId} </span>
        <br/>
        <select name="propertyId" value={this.state.propertyId} onChange={this.handleChange.bind(this)}>
           {this.props.editedTimeSeries.get("agentDescription").get("propertyDescriptions").map(function(property) {
            return <option value={property.get("id")}> {property.get("name")} </option>;
          })}
        </select>
        <br/>
        <select name="aggregate" value={this.state.aggregate} onChange={this.handleChange.bind(this)}>
          <option value="max">max</option>
          <option value="min">min</option>
          <option value="average">average</option>
          <option value="count">count</option>
          <option value="sum">sum</option>
        </select>
        <br/>
        <button onClick={() => this.addTimeSeries()}>add time series</button>
      </div>
    );
  }

}
