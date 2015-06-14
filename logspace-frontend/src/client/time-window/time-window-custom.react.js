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
import DateRangePicker from 'react-daterange-picker'
import Tabs from 'react-simpletabs'
import moment from 'moment'

import {selectCustomDate, selectPredefinedDate, selectDynamicDate} from './actions';
import {GAPS,selections} from './constants'

require('./time-window.styl')

export default class TimeWindowCustom extends Component {

  
  handleChange(event) {
    var newState = {
      custom: {
        dateRange: {} 
      }
    };

    newState['custom']['dateRange'][event.target.name] = event.target.value

    console.log(newState)

    this.setState({
      localState: this.state.localState.mergeDeep(newState)
    })
  }
  
  submitCustom() {
    selectCustomDate(
      {
        start: this.state.start,
        end: this.state.end,
        gap: this.state.gap
      }
    );  
  }

  handleSelect(range, states) {
    this.setState({
      localState: this.state.localState.mergeDeep({
        custom: {
          dateRange: moment().range(range.start, range.end)
        }
      })
    })
  }

  submitDynamic() {
    selectDynamicDate(this.state.dynamic.count, this.state.dynamic.unit, this.state.dynamic.gap)
  }
  
  render() {
    const state = this.state.localState.toJS()
    console.log(state)

    return (
      <div>
        <DateRangePicker
          numberOfCalendars={2}
          selectionType="range"
          singleDateRange={true}
          firstOfWeek={1}
          value={state.custom.dateRange}
          onSelect={this.handleSelect.bind(this)} />
    
        <form name="time" >
          <table>
            <tr>
              <td> Start</td>
              <td>  <b>{state.custom.dateRange.start.format("YYYY-MM-DD")}</b> </td>
              <td><input name="start" value={state.custom.time.start.format("HH:mm:ss")} onChange={this.handleChange.bind(this)}/> </td>
            </tr>
            <tr>
              <td> End</td>
              <td>  <b>{state.custom.dateRange.end.format("YYYY-MM-DD")}</b> </td>
              <td><input name="start" value={state.custom.time.end.format("HH:mm:ss")} onChange={this.handleChange.bind(this)}/> </td>
            </tr>
             <tr>
              <td> Gap</td>
              <td> </td>
              <td>
                <select name="gap" value={state.gap} onChange={this.handleChange.bind(this)}>
                  <option value={GAPS.second}>pro Sekunde</option>
                  <option value={GAPS.minute}>pro Minute</option>
                  <option value={GAPS.hour}>pro Stunde</option>
                  <option value={GAPS.day}>pro Tag</option>
                  <option value={GAPS.week}>pro Woche</option>
                  <option value={GAPS.month}>pro Monat</option>
                  <option value={GAPS.year}>pro Jahr</option>
                </select>  
              </td>
            </tr>
            <tr>
              <td> </td>
              <td> </td>
              <td>
                <input type="button" value="set time" onClick={this.submitCustom.bind(this)} /> 
              </td>
            </tr>
          </table>
        </form>
      </div>
    );
  }

}
