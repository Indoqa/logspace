/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import {default as LiteDropdown} from 'react-lite-dropdown'
import 'react-lite-dropdown/src/style.css'

const UNITS = [
  {value: 604800, name: 'Week'},
  {value: 86400, name: 'Day'},
  {value: 3600, name: 'Hour'},
  {value: 60, name: 'Minute'},
]

export default class Header extends React.Component {

  constructor(props) {
    super(props)

    this.state = {
      chartTypeDropdownShown: false
    }
  }

  getDurationDisplayName(duration) {
    for (let index = 0; index < UNITS.length; index++) {
      const unit = UNITS[index]

      if (duration > unit.value) {
        return `${duration / unit.value} ${unit.name}s`
      }

      if (duration === unit.value) {
        return `1 ${unit.name}`
      }
    }

    return duration
  }

  getDurationOption(duration) {
    return <div className={'item'} onClick={() => this.props.setDuration(duration)}>{this.getDurationDisplayName(duration)}</div>
  }

  toggleChartTypeDropdownShown() {
    const currentValue = this.state.chartTypeDropdownShown
    this.setState({chartTypeDropdownShown: !currentValue})
  }

  renderPlayControls() {
    if (this.props.autoPlay.get('running')) {
      return (
        <span>
          <span className="option pause" onClick={() => this.props.toggleAutoPlay()} />
          <span className="option progress" onClick={this.props.loadAgentActivities}>
            <span id="progress"> {this.props.autoPlay.get('countdown')} </span>
          </span>
        </span>
      )
    }

    return (
      <span>
        <span className="option play" onClick={() => this.props.toggleAutoPlay()} />
        <span className="option refresh" onClick={this.props.loadAgentActivities} />
      </span>
    )
  }

  render() {
    return (
      <div className="chart-header">
        <div className="chart-options">
          {this.renderPlayControls()}
          <LiteDropdown
            displayText={this.getDurationDisplayName(this.props.duration)}
            defaultText={'not used'}
            show={this.state.chartTypeDropdownShown}
            onToggle={() => this.toggleChartTypeDropdownShown()}
            name={'css-hook-demo'}
          >
              {this.getDurationOption(300)}
              {this.getDurationOption(900)}
              {this.getDurationOption(1800)}
              {this.getDurationOption(3600)}
              {this.getDurationOption(7200)}
              {this.getDurationOption(21600)}
              {this.getDurationOption(43200)}
              {this.getDurationOption(86400)}
              {this.getDurationOption(604800)}
              {this.getDurationOption(2419200)}
          </LiteDropdown>
        </div>
      </div>
    )
  }
}

Header.propTypes = {
  autoPlay: React.PropTypes.object.isRequired,
  duration: React.PropTypes.number.isRequired,

  loadAgentActivities: React.PropTypes.func.isRequired,
  setDuration: React.PropTypes.func.isRequired,
  toggleAutoPlay: React.PropTypes.func.isRequired,
}
