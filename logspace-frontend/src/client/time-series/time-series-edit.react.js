/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react';
import PureComponent from '../components/purecomponent.react';
import TimeSeriesLabel from './time-series-label.react';

import shallowEqual from 'react/lib/shallowEqual';
import {COLORS, TYPES} from './constants';
import {onTimeSeriesSaved, onTimeSeriesPropertyChanged, onTimeSeriesDeleted} from './actions';

require('./time-series-edit.styl')

export default class EditTimeSeries extends PureComponent {
  handleChange(event) {
    onTimeSeriesPropertyChanged(event.target.name, event.target.value)
  }

  render() {
    var agentDescription = this.props.editedTimeSeries.get("newItem");
    var me = this;

    var usedColors = this.props.timeSeries.map(function(item) {
      if (item.get("id") === agentDescription.get("id")) {
        return ""
      }

      return item.get("color")
    });
 
    return (
      <div>
        <TimeSeriesLabel timeSeries={agentDescription} />
        <hr/>
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
                onChange={me.handleChange.bind(me)}
                disabled={property.get("propertyType")==="STRING"}>
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
          var colorSyle = {
            backgroundColor: color
          }
          return (
            <div className='color-option' style={colorSyle} >
              <input
                type="radio"
                name="color"
                value={color}
                checked={color == agentDescription.get("color")}
                onChange={me.handleChange.bind(me)}
                disabled={color != agentDescription.get("color") && usedColors.indexOf(color) > -1}>
                </input>
            </div>
          )
        })}
        <div className='clearer'/>
        <br/>
        <b>Define Scale: </b>
        <br/>
        <input name='scaleMin' value={agentDescription.get("scaleMin")} onChange={this.handleChange.bind(this)}/> <nbsp/>-<nbsp/> <input name='scaleMax' value={agentDescription.get("scaleMax")} onChange={this.handleChange.bind(this)}/>
        <hr/>
        <button onClick={() => onTimeSeriesSaved()}>Save time series</button>
        <button 
          className={(agentDescription.get("id") != null) ? 'delete-visible' : 'delete-hidden'} 
          onClick={() =>  onTimeSeriesDeleted(agentDescription.get('id'))}>Delete time series
        </button>
      </div>
    );
  }

}
