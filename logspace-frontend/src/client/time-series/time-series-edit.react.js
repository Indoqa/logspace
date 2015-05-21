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
import {COLORS} from './constants';
import {onTimeSeriesSaved, onTimeSeriesPropertyChanged} from './actions';

export default class EditTimeSeries extends PureComponent {
  handleChange(event) {
    onTimeSeriesPropertyChanged(event.target.name, event.target.value)
  }

  render() {
    console.log("render")

    var agentDescription = this.props.editedTimeSeries.get("newItem");
    var me = this;
    var header = "Edit Time Series";

    if (agentDescription.get("id") == null) {
      header = "New Time Series";
    }

    return (
      <div>
        <h2> {header} </h2>
        <div> Agent: {agentDescription.get("name")} </div>
        <div> Space: {agentDescription.get("space")} </div>
        <div> System: {agentDescription.get("system")} </div>
        <br/>
        <b>Select Property:</b>
        <br/>
        {agentDescription.get("propertyDescriptions").map(function(property) {
          return (
            <div>
              <input
                type="radio"
                name="propertyId"
                value={property.get("id")}
                checked={property.get("id") == agentDescription.get("propertyId")}
                onChange={me.handleChange.bind(me)}>
                </input>
                <nbsp/>
                <span> {property.get("name")} </span>
              <br/>
            </div>
          )
        })}
        <br/>
        <b>Select Aggregation: </b>
        <br/>
        <select name="aggregate" value={agentDescription.get("aggregate")} onChange={this.handleChange.bind(this)}>
          <option value="max">max</option>
          <option value="min">min</option>
          <option value="avg">average</option>
          <option value="count">count</option>
          <option value="sum">sum</option>
        </select>
        <br/>
        <br/>
        <b>Select Color: </b>
        <br/>
        {COLORS.map(function(color) {
          return (
            <span>
              <input
                type="radio"
                name="color"
                value={color}
                checked={color == agentDescription.get("color")}
                onChange={me.handleChange.bind(me)}>
                </input>
                <nbsp/>
                <span> {color} </span>
            </span>
          )
        })}
        <br/>
        <hr/>
        <button onClick={() => onTimeSeriesSaved()}>Save time series</button>
      </div>
    );
  }

}
