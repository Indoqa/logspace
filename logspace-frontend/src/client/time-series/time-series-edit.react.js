/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react';
import Component from '../components/component.react';
import TimeSeriesLabel from './time-series-label.react';

import shallowEqual from 'react/lib/shallowEqual';
import {COLORS, TYPES, SCALES} from './constants';
import {onTimeSeriesSaved, onTimeSeriesPropertyChanged, onTimeSeriesDeleted} from './actions';

require('./time-series-edit.styl')

export default class EditTimeSeries extends Component {

  handleChange(event) {
    onTimeSeriesPropertyChanged(event.target.name, event.target.value)
  }

  getCustomScaleInput(agentDescription) {
    if (agentDescription.get("scaleType") != 'custom') {
      return <div/>
    }

    return (<span>
      <input name='scaleMin'
        value={agentDescription.get("scaleMin")}
        onChange={this.handleChange.bind(this)}/>
      <nbsp/>-<nbsp/>
      <input name='scaleMax'
        value={agentDescription.get("scaleMax")}
        onChange={this.handleChange.bind(this)}/>
      </span>)
  }

   getPropertyScaleInput(agentDescription) {
    const propertyScale = SCALES[agentDescription.get("propertyId")]

    if (propertyScale == null) {
      return <div/>
    }

    const savePropertyScale = () => {
      onTimeSeriesPropertyChanged('scaleType', 'property')
      onTimeSeriesPropertyChanged('scaleMin', propertyScale.min)
      onTimeSeriesPropertyChanged('scaleMax', propertyScale.max)
    }

    return (<div>
        <input
          type="radio"
          name="scaleMin"
          value={'auto'}
          checked={agentDescription.get("scaleType") === 'property'}
          onChange={savePropertyScale}
          >
        </input> {propertyScale.label} ({propertyScale.min} - {propertyScale.max})
      </div>)
  }

  render() {
    const agentDescription = this.props.editedTimeSeries.get("newItem");
    const me = this;

    const propertyScaleInput = this.getPropertyScaleInput(agentDescription)
    const customScaleInput = this.getCustomScaleInput(agentDescription)

    const usedColors = this.props.timeSeries.map(function(item) {
      if (item.get("id") === agentDescription.get("id")) {
        return ""
      }

      return item.get("color")
    });

    return (
      <div>
        <div className='time-series-label-wrapper'>
          <TimeSeriesLabel timeSeries={agentDescription} />
        </div>

        <div className='spacer' />

        <b>Select Property</b>
        <br/>
        {agentDescription.get("propertyDescriptions").map(function(property) {
          const propertyId = property.get("id")
          return (
            <div key={propertyId}>
              <input
                type="radio"
                name="propertyId"
                value={propertyId}
                checked={propertyId == agentDescription.get("propertyId")}
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
        <b>Select Aggregation </b>
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

        <b>Select Color </b>
        <br/>
        {COLORS.map(function(color) {
          const colorSyle = {
            backgroundColor: color
          }
          return (
            <div key={color} className='color-option' style={colorSyle} >
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
        <b>Define Scale</b>
        <br/>
        <input
          type="radio"
          name="scaleType"
          value={'auto'}
          checked={agentDescription.get("scaleType") == 'auto'}
          onChange={me.handleChange.bind(me)}>
        </input> Data (use min/max of result)
        <br/>
        {propertyScaleInput}
        <input
          type="radio"
          name="scaleType"
          value={'custom'}
          checked={agentDescription.get("scaleType") == 'custom'}
          onChange={me.handleChange.bind(me)}>
        </input> Custom {customScaleInput}

        <div className='buttons'>
          <button className='waves-effect waves-light btn' onClick={() => onTimeSeriesSaved()}>Save time series</button>
          <button
            className={(agentDescription.get("id") != null) ? 'delete-visible waves-effect waves-light btn btn-highlight' : 'delete-hidden'}
            onClick={() =>  onTimeSeriesDeleted(agentDescription.get('id'))}>Delete time series
          </button>
        </div>
      </div>
    );
  }

}
