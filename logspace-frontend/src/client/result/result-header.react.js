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

import {onEditableState} from '../editable/actions'
import {saveChartTitle, setChartType} from './actions'

export default class Header extends Component {

  onChartTitleSaved(title, hide) {
    saveChartTitle(title)
    hide()
  }

  render() {
    return (
      <div className='chart-header'>
        <div className='chart-options'>
          <select onChange={(e) => setChartType(e.target.value)}
              value={this.props.chartType}>
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
