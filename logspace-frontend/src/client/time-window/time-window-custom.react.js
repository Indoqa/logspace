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
import DateRangePicker from 'react-daterange-picker'
import GapSelection from './time-window-gapselection.react.js'
import TimePicker from 'react-time-picker'

import {selectCustomDate} from './actions';
import {units,selections} from './constants'

require('./time-window.styl')

export default class TimeWindowCustom extends Component {
  constructor(props) {
    super(props);

    this.state = {
      localState: Immutable.fromJS({
        dateRange: moment.range(props.timeWindow.start(), props.timeWindow.end()),
        time: {
          start: props.timeWindow.start().format("HH:mm:ss"),
          end: props.timeWindow.end().format("HH:mm:ss")
        },
        gap: props.timeWindow.gap
      })
    }
  }

  onTimeChange(field, value) {
    var newState = {
      time:{}
    };

    newState.time[field] = value

    this.setState({
      localState: this.state.localState.mergeDeep(newState)
    })
  }

  onTimeReset() {
    var newState = {
      time:{
        start: '00:00:00',
        end: '23:59:59'
      }
    };

    this.setState({
      localState: this.state.localState.mergeDeep(newState)
    })
  }

  onDateRangeChange(range, states) {
    this.setState({
      localState: this.state.localState.mergeDeep({
        dateRange: moment().range(range.start, range.end)
      })
    })
  }

  onGapChange(value) {
    this.setState({
      localState: this.state.localState.merge({
        gap: value
      })
    })
  }

  submitCustom() {
    const state = this.state.localState.toJS()

    const startDate = state.dateRange.start
    const endDate = state.dateRange.end

    const startTime = moment(state.time.start, "HH:mm:ss");
    const endTime = moment(state.time.end, "HH:mm:ss");

    startDate.hour(startTime.hour())
    startDate.minute(startTime.minute())
    startDate.second(startTime.second())

    endDate.hour(endTime.hour())
    endDate.minute(endTime.minute())
    endDate.second(endTime.second())

    selectCustomDate(startDate, endDate, Immutable.fromJS(state.gap))
  }

  render() {
    const state = this.state.localState.toJS()

    return (
      <div className='current'>
        <div className='selection'>
          <div className='submit' >
            <span className='intro'>UNIT</span>
            <GapSelection value={this.state.localState.get('gap')} onChange={this.onGapChange.bind(this)}/>
            <button className='waves-effect waves-light btn btn-small' onClick={this.submitCustom.bind(this)}>
              Apply
            </button>
          </div>
          <div className='date'>
            <span className='day'>{state.dateRange.start.format('YYYY-MM-DD')}</span><br/>
            <span className='time'>{state.time.start} </span>
          </div>
          <div className='date'>
            <span className='day'>{state.dateRange.end.format('YYYY-MM-DD')}</span><br/>
            <span className='time'>{state.time.end} </span>
          </div>
        </div>
        <DateRangePicker
          numberOfCalendars={2}
          selectionType="range"
          singleDateRange={true}
          firstOfWeek={1}
          value={state.dateRange}
          onSelect={this.onDateRangeChange.bind(this)} />
        <TimePicker
          value={state.time.start}
          style={{float: 'left', width: '150px', padding: 5, border: 'none', marginLeft: '45px'}}
          onChange={(value) => this.onTimeChange('start', value)}
        />
        <button
          onClick={() => this.onTimeReset()}
          style={{float: 'left', width: '50', padding: 5, marginLeft: '20px', marginTop: '30px'}}>reset</button>
        <TimePicker
          value={state.time.end}
          style={{float: 'left', width: '150', padding: 5, border: 'none', marginLeft: '20px'}}
          onChange={(value) => this.onTimeChange('end', value)}
        />
      </div>
    );
  }

}
