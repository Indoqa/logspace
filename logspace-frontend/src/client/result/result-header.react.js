/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import classnames from 'classnames'

import Component from '../components/component.react'
import Editable from '../editable/editable.react'
import Chart from './result-chart.react'
import moment from 'moment'

import {onEditableState} from '../editable/actions'
import {saveChartTitle, setChartType, refreshResult, setAutoPlay} from './actions'

require ('./result-header.styl')

export default class Header extends Component {

  constructor(props) {
    super(props)
    
    var me = this
    setInterval(function(){me.onProgress()}, 500)
  }

  onProgress() {
    const label = document.getElementById('progress')

    if (!label) {
      return
    }

    if (!this.props.autoPlaySchedule) {
      label.innerHTML = 15
      return
    }

    const difference = moment().diff(this.props.autoPlaySchedule, 'seconds') 
    label.innerHTML = (15-difference)
  }

  onChartTitleSaved(title, hide) {
    saveChartTitle(title)
    hide()
  }

  getPlayControls() {
    if (this.props.autoPlay) {
           return (
        <span>
          <span className='option pause' onClick={() => setAutoPlay(false)}/>
          <span className='option progress' onClick={refreshResult}>
            <span id='progress'> 15 </span>
          </span>
        </span>
      )
    }

    return (
      <span>
        <span className='option play' onClick={() => setAutoPlay(true)} />
        <span className='option refresh' onClick={refreshResult}/>
      </span>  
    )
  }

  render() {
    const playControls = this.getPlayControls()
 
    return (
      <div className='chart-header'>
        <div className='chart-options'>
          {playControls}
          <select onChange={(e) => setChartType(e.target.value)} value={this.props.chartType}>
            <option value={'bar'}>bar</option>
            <option value={'line'}>line</option>
            <option value={'spline'}>spline</option>
            <option value={'step'}>step</option>
            <option value={'area'}>area</option>
            <option value={'area-spline'}>area-spline</option>
            <option value={'area-step'}>area-step</option>
            <option value={'scatter'}>scatter</option>
          </select>
        </div>
        <div className="chart-title">
          <Editable
            defaultValue={this.props.chartTitle}
            disabled={false}
            id={'result'}
            isRequired
            maxLength={200}
            name={'chartTitle'}
            onSave={(title, hide) => (this.onChartTitleSaved(title, hide))}
            onState={onEditableState}
            state={this.props.chartTitleEditable}
          >
            <label>{this.props.chartTitle}</label>
          </Editable>
        </div>  
      </div>  
    )
  }
}
