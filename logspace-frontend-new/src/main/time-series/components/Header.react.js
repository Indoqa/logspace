/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'

import {Box, Flex} from 'reflexbox'
import {Button, Dropdown, DropdownMenu, NavItem} from 'rebass'
import RefreshIcon from '../../app/components/autoplay/RefreshIcon.react'
import RunIcon from '../../app/components/autoplay/RunIcon.react'
import {FontAwesome} from 'indoqa-rebass-components'
import 'font-awesome/css/font-awesome.css'

import Editable from '../../app/components/editable/Editable.react'

export default class Header extends React.Component {

  constructor(props) {
    super(props)

    this.state = {
      chartTypeDropdownShown: false
    }
  }

  onChartTitleSaved(title, hide) {
    this.props.saveChartTitle(title)
    hide()
  }

  setChartType(chartType) {
    this.setState({chartTypeDropdownShown: false})
    this.props.setChartType(chartType)
  }

  toggleChartTypeDropdownShown() {
    const currentValue = this.state.chartTypeDropdownShown
    this.setState({chartTypeDropdownShown: !currentValue})
  }

  render() {
    return (
      <Box auto>
        <Flex align="center" justify="flex-end">
          <Button _className="AutoPlayButton" onClick={() => this.props.toggleAutoPlay()} >
            <RunIcon running={this.props.autoPlay.get('running')} />
          </Button>
          <Button _className="ReloadButton" >
            <RefreshIcon countdown={this.props.autoPlay.get('countdown')} loading={this.props.loading} onClick={this.props.refreshResult} />
          </Button>
          <Dropdown>
            <Button _className="ChartTypeButton" onClick={() => this.toggleChartTypeDropdownShown()} >
              {this.props.chartType}
              <FontAwesome icon="caret-down" className="FontAwesomeToggle" onClick={() => this.toggleChartTypeDropdownShown()} />
            </Button>
            <DropdownMenu open={this.state.chartTypeDropdownShown} onDismiss={() => this.toggleChartTypeDropdownShown()}>
              <NavItem onClick={() => this.setChartType('bar')}>Bar</NavItem>
              <NavItem onClick={() => this.setChartType('line')}>Line</NavItem>
              <NavItem onClick={() => this.setChartType('spline')}>Spline</NavItem>
              <NavItem onClick={() => this.setChartType('step')}>Step</NavItem>
              <NavItem onClick={() => this.setChartType('area')}>Area</NavItem>
              <NavItem onClick={() => this.setChartType('area-spline')}>Area spline</NavItem>
              <NavItem onClick={() => this.setChartType('area-step')}>Area step</NavItem>
              <NavItem onClick={() => this.setChartType('scatter')}>Scatter</NavItem>
            </DropdownMenu>
          </Dropdown>
        </Flex>
      </Box>
    )
  }
}

Header.propTypes = {
  autoPlay: PropTypes.object.isRequired,
  loading: PropTypes.bool,
  chartType: PropTypes.string.isRequired,
  chartTitle: PropTypes.string.isRequired,
  chartTitleEditable: PropTypes.object,

  updateEditableState: PropTypes.func.isRequired,
  saveChartTitle: PropTypes.func.isRequired,
  setChartType: PropTypes.func.isRequired,
  refreshResult: PropTypes.func.isRequired,
  toggleAutoPlay: PropTypes.func.isRequired,
}
