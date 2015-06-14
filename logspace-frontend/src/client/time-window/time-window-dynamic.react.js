/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
 
import React from 'react';
import Immutable from 'immutable'
import Component from '../components/component.react'
import moment from 'moment'

import {selectDynamicDate} from './actions';
import {GAPS,selections} from './constants'

require('./time-window.styl')

export default class TimeWindowDynamic extends Component {

  constructor(props) {
    super(props);
    
    this.state = { 
      localState: Immutable.fromJS({
        count: 60,
        unit: 'minutes',
        gap: GAPS.minute
      }) 
    }
  }

  handleChange(event) {
    var newState = {};
    newState[event.target.name] = event.target.value

    this.setState({
      localState: this.state.localState.mergeDeep(newState)
    })
  }
  
  render() {
    const state = this.state.localState.toJS()
  
    return (
      <div>
        last 
        <input name="count" value={state.count} onChange={this.handleChange.bind(this)} size='5'/> 
        <select name="unit" value={state.unit} onChange={this.handleChange.bind(this)}>
          <option value='seconds'>seconds</option>
          <option value='minutes'>minutes</option>
          <option value='days'>days</option>
        </select>
        <br/>
        Gap:  
         <select name="gap" value={state.gap} onChange={this.handleChange.bind(this)}>
          <option value={GAPS.second}>seconds</option>
          <option value={GAPS.minute}>minutes</option>
          <option value={GAPS.hour}>hours</option>
          <option value={GAPS.day}>days</option>
        </select>    
        <br/>  
        <input type="button" value="go" onClick={() => selectDynamicDate(state.count, state.unit, state.gap)} /> 
      </div>
    )
  }
}
