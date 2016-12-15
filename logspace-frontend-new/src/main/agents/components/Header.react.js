/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import RefreshIcon from '../../app/components/autoplay/RefreshIcon.react'
import RunIcon from '../../app/components/autoplay/RunIcon.react'
import {Button, Dropdown, DropdownMenu, NavItem} from 'rebass'
import {Flex, Box} from 'reflexbox'
import {FontAwesome} from 'indoqa-rebass-components'
import 'font-awesome/css/font-awesome.css'

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
      durationDropdownShown: false
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

  setDuration(duration) {
    this.setState({durationDropdownShown: false})
    this.props.setDuration(duration)
  }

  getDurationOption(duration) {
    return <NavItem onClick={() => this.setDuration(duration)}>{this.getDurationDisplayName(duration)}</NavItem>
  }

  toggledurationDropdownShown() {
    const currentValue = this.state.durationDropdownShown
    this.setState({durationDropdownShown: !currentValue})
  }

  render() {
    return (
      <Box auto>
        <Flex align="center" justify="flex-end">
          <Button _className="AutoPlayButton" >
            <RunIcon running={this.props.autoPlay.get('running')} onClick={() => this.props.toggleAutoPlay()} />
          </Button>
          <Button _className="ReloadButton" >
            <RefreshIcon countdown={this.props.autoPlay.get('countdown')} loading={this.props.loading} onClick={this.props.loadAgentActivities} />
          </Button>
          <Dropdown>
            <Button _className="DurationButton" onClick={() => this.toggledurationDropdownShown()} >
              {this.getDurationDisplayName(this.props.duration)}
              <FontAwesome icon="caret-down" className="FontAwesomeToggle" onClick={() => this.toggledurationDropdownShown()} />
            </Button>
            <DropdownMenu open={this.state.durationDropdownShown} onDismiss={() => this.toggledurationDropdownShown()}>
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
            </DropdownMenu>
          </Dropdown>
        </Flex>
      </Box>
    )
  }
}

Header.propTypes = {
  autoPlay: React.PropTypes.object.isRequired,
  duration: React.PropTypes.number.isRequired,
  loading: React.PropTypes.bool.isRequired,

  loadAgentActivities: React.PropTypes.func.isRequired,
  setDuration: React.PropTypes.func.isRequired,
  toggleAutoPlay: React.PropTypes.func.isRequired,
}
