/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React, {PropTypes} from 'react'
import Immutable from 'immutable'
import moment from 'moment'
import DateRangePicker from 'react-daterange-picker'
import TimePicker from 'react-time-picker'
import TimeWindowGapSelection from './TimeWindowGapSelection.react.js'

require('./TimeWindow.styl')

export default class TimeWindowCustom extends React.Component {
  constructor(props) {
    super(props)

    const selection = props.timeWindow.toJS()

    this.state = {
      localState: Immutable.fromJS({
        dateRange: moment.range(selection.start(), selection.end()),
        time: {
          start: selection.start().format('HH:mm'),
          end: selection.end().format('HH:mm')
        },
        gap: selection.gap
      })
    }
  }

  onTimeChange(field, value) {
    const newState = {
      time: {}
    }

    newState.time[field] = value

    this.setState({
      localState: this.state.localState.mergeDeep(newState)
    })
  }

  onTimeReset() {
    const newState = {
      time: {
        start: '00:00',
        end: '23:59'
      }
    }

    this.setState({
      localState: this.state.localState.mergeDeep(newState)
    })
  }

  onDateRangeChange(range) {
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

    if (state.gap.amount < 1) {
      return
    }

    const startDate = state.dateRange.start
    const endDate = state.dateRange.end

    const startTime = moment(state.time.start, 'HH:mm')
    const endTime = moment(state.time.end, 'HH:mm')

    startDate.hour(startTime.hour())
    startDate.minute(startTime.minute())

    endDate.hour(endTime.hour())
    endDate.minute(endTime.minute())

    this.props.selectCustomDate(startDate, endDate, Immutable.fromJS(state.gap))
  }

  render() {
    const state = this.state.localState.toJS()

    return (
      <div className="current">
        <div className="selection">
          <div className="submit">
            <span className="intro">GAP</span>
            <TimeWindowGapSelection value={this.state.localState.get('gap')} onChange={(value) => this.onGapChange(value)} />
            <button className="waves-effect waves-light btn btn-small" onClick={(event) => this.submitCustom(event)}>
              Apply
            </button>
          </div>
          <div className="date">
            <span className="day">{state.dateRange.start.format('YYYY-MM-DD')}</span><br />
            <span className="time">{state.time.start} </span>
          </div>
          <div className="date">
            <span className="day">{state.dateRange.end.format('YYYY-MM-DD')}</span><br />
            <span className="time">{state.time.end} </span>
          </div>
        </div>
        <DateRangePicker
          numberOfCalendars={2}
          selectionType="range"
          singleDateRange
          firstOfWeek={1}
          value={state.dateRange}
          onSelect={(event) => this.onDateRangeChange(event)}
        />
        <TimePicker
          value={state.time.start}
          style={{float: 'left', width: '150px', padding: 5, border: 'none', marginLeft: '45px'}}
          format="HH:mm"
          onChange={(value) => this.onTimeChange('start', value)}
        />
        <a
          className="waves-effect waves-light btn inverted"
          onClick={() => this.onTimeReset()}
          style={{float: 'left', width: '50', padding: 5, marginLeft: '20px', marginTop: '22px'}}
        >
          reset
        </a>
        <TimePicker
          value={state.time.end}
          style={{float: 'left', width: '150', padding: 5, border: 'none', marginLeft: '20px'}}
          format="HH:mm"
          onChange={(value) => this.onTimeChange('end', value)}
        />
      </div>
    )
  }
}

TimeWindowCustom.propTypes = {
  timeWindow: PropTypes.object.isRequired,
  selectCustomDate: PropTypes.func.isRequired
}
