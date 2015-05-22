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
import {COLORS, TYPES} from './constants';
import {onTimeSeriesSaved, onTimeSeriesPropertyChanged, onTimeSeriesDeleted} from './actions';

require('./time-series-edit.styl')

export default class EditTimeSeries extends PureComponent {
  handleChange(event) {
    onTimeSeriesPropertyChanged(event.target.name, event.target.value)
  }

  render() {
    console.log("render")

    var agentDescription = this.props.editedTimeSeries.get("newItem");
    var me = this;
 
    return (
      <div>
        <div> {agentDescription.get("space")} {agentDescription.get("system")} </div>
        <div> {agentDescription.get("name")} </div>
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
        <b>Select Chart Type: </b>
        <br/>
        <select name="type" value={agentDescription.get("type")} onChange={this.handleChange.bind(this)}>
           {TYPES.map(function(type) {
            return <option value={type}>{type}</option>
          })}
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
        <button 
          className={(agentDescription.get("id") != null) ? 'delete-visible' : 'delete-hidden'} 
          onClick={() =>  onTimeSeriesDeleted(this.props.item)}>Delete time series
        </button>
      </div>
    );
  }

}
