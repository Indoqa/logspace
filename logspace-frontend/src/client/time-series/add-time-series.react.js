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

    this.state = {agentId: "deep-search-wwkswm/commit", propertyId: "long_property_warmup_time", aggregate: "sum"};
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

  shouldComponentUpdate(nextProps, nextState) {
    return !shallowEqual(this.state, nextState);
  }

  render() {
    return (
      <div>
        <input name="agentId" value={this.state.agentId} onChange={this.handleChange.bind(this)}/> <br/>
        <input name="propertyId" value={this.state.propertyId} onChange={this.handleChange.bind(this)}/> <br/>
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
