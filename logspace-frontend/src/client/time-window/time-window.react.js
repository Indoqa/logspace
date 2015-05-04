/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
 
import React from 'react';
import PureComponent from '../components/purecomponent.react';
import {onTimeWindowChange} from './actions';

export default class TimeWindow extends PureComponent {
  
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
          <input name="gap" value={this.state.gap} onChange={this.handleChange.bind(this)}/> <br/>
          <input type="button" value="set time" onClick={this.handleSubmit.bind(this)} />  
        </form>
      </div>
    );
  }

}
