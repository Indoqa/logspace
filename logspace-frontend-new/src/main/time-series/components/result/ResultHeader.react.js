/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'

import Editable from '../../../app/components/editable/Editable.react'

import {default as LiteDropdown} from 'react-lite-dropdown'
import 'react-lite-dropdown/src/style.css'

require('./ResultHeader.styl')

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

  getPlayControls() {
    if (this.props.autoPlay.get('running')) {
      return (
        <span>
          <span className="option pause" onClick={() => this.props.toggleAutoPlay()} />
          <span className="option progress" onClick={this.props.refreshResult}>
            <span id="progress"> {this.props.autoPlay.get('countdown')} </span>
          </span>
        </span>
      )
    }

    return (
      <span>
        <span className="option play" onClick={() => this.props.toggleAutoPlay()} />
        <span className="option refresh" onClick={this.props.refreshResult} />
      </span>
    )
  }

  toggleChartTypeDropdownShown() {
    const currentValue = this.state.chartTypeDropdownShown
    this.setState({chartTypeDropdownShown: !currentValue})
  }

  render() {
    const playControls = this.getPlayControls()

    return (
      <div className="chart-header">
        <div className="chart-options">
          {playControls}
          <LiteDropdown
            displayText={this.props.chartType}
            defaultText={'not used'}
            show={this.state.chartTypeDropdownShown}
            onToggle={() => this.toggleChartTypeDropdownShown()}
            name={'css-hook-demo'}
          >
              <div className={'item'} onClick={() => this.props.setChartType('bar')}>Bar</div>
              <div className={'item'} onClick={() => this.props.setChartType('line')}>Line</div>
              <div className={'item'} onClick={() => this.props.setChartType('spline')}>Spline</div>
              <div className={'item'} onClick={() => this.props.setChartType('step')}>Step</div>
              <div className={'item'} onClick={() => this.props.setChartType('area')}>Area line</div>
              <div className={'item'} onClick={() => this.props.setChartType('area-spline')}>Area spline</div>
              <div className={'item'} onClick={() => this.props.setChartType('area-step')}>Area step</div>
              <div className={'item'} onClick={() => this.props.setChartType('scatter')}>Scatter</div>
          </LiteDropdown>
        </div>
        <div className="chart-title">
          <Editable
            defaultValue={this.props.chartTitle}
            disabled={false}
            id={'result'}
            isRequired
            maxLength={200}
            msg={(key) => key}
            name={'chartTitle'}
            onSave={(title, hide) => (this.onChartTitleSaved(title, hide))}
            onState={this.props.updateEditableState}
            state={this.props.chartTitleEditable}
          >
            <label>{this.props.chartTitle}</label>
          </Editable>
        </div>
      </div>
    )
  }
}

Header.propTypes = {
  autoPlay: PropTypes.object.isRequired,
  chartType: PropTypes.string.isRequired,
  chartTitle: PropTypes.string.isRequired,
  chartTitleEditable: PropTypes.object,
  updateEditableState: PropTypes.func.isRequired,
  saveChartTitle: PropTypes.func.isRequired,
  setChartType: PropTypes.func.isRequired,
  refreshResult: PropTypes.func.isRequired,
  toggleAutoPlay: PropTypes.func.isRequired
}
