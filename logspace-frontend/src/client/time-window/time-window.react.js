/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
 
import React from 'react';
import Component from '../components/component.react';
import {onTimeWindowChange} from './actions';
import {GAPS} from './constants'

export default class TimeWindow extends Component {
  
  constructor(props) {
    super(props);
    
    this.state = {
      start: props.timeWindow.get('start'),
      end: props.timeWindow.get('end'),
      gap: props.timeWindow.get('gap')
    }
  }

  handleChange(event) {
    var newState = {};
    newState[event.target.name] = event.target.value;
    this.setState(newState);   
  }
  
  handleSubmit() {
    onTimeWindowChange(
      {
        start: this.state.start,
        end: this.state.end,
        gap: this.state.gap
      }
    );  
  }
  
  render() {
    return (
      <div>
        <form name="time" >
          <input name="start" value={this.state.start} onChange={this.handleChange.bind(this)}/> <br/>
          <input name="end" value={this.state.end} onChange={this.handleChange.bind(this)}/> <br/>
          <select name="gap" value={this.state.gap} onChange={this.handleChange.bind(this)}>
            <option value={GAPS.second}>pro Sekunde</option>
            <option value={GAPS.minute}>pro Minute</option>
            <option value={GAPS.hour}>pro Stunde</option>
            <option value={GAPS.day}>pro Tag</option>
            <option value={GAPS.week}>pro Woche</option>
            <option value={GAPS.month}>pro Monat</option>
            <option value={GAPS.year}>pro Jahr</option>
          </select>  
          <input type="button" value="set time" onClick={this.handleSubmit.bind(this)} /> 
        </form>
      </div>
    );
  }

}
