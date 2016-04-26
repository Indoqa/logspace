/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react';
import classnames from 'classnames';

import Component from '../components/component.react';

import TimeSeriesLabel from './time-series-label.react';
import {onTimeSeriesDeleted, onEditTimeSeries} from './actions';
import {isSubitem} from './constants';

require('./time-series-item.styl')

export default class TimeSeriesItem extends Component {

  getAxisLabel(item) {
    if (isSubitem(item.get('scaleType'))) {
      return <span/>
    }

    const classNames = 'axis axis-' + this.props.axis
    return  <div className={classNames} title={this.getAxisTooltip()}/>
  }

  getAxisTooltip() {
    if (this.props.axis == 1) {
      return 'Scale is shown on left y axis'
    }

    if (this.props.axis == 2) {
      return 'Scale is shown on right y axis'
    }
  }

  render() {
    const bgStyle = {
      backgroundColor: this.props.item.get('color')
    }
    const item = this.props.item

    const axisLabel = this.getAxisLabel(item)

    return (
      <div className={createTimeSeriesClassName(item)} onClick={() => onEditTimeSeries(item)}>
        <div className='color' style={bgStyle}></div>
        <div className='inner'>
          {axisLabel}
          <TimeSeriesLabel timeSeries={item} />
          <span className='property'>{item.get('aggregate')} of {cleanPropertyName(item.get('propertyId'))}</span>
        </div>
      </div>
    )
  }
}

function createTimeSeriesClassName(item) {
  return classnames('time-series-item', {
    subentry: isSubitem(item.get('scaleType')),
    mainentry: !isSubitem(item.get('scaleType'))
  })
}

export function cleanPropertyName(name) {
  const pattern = /[\w]*?_[\w]*?_(.*)/
  const result = pattern.exec(name)

  if(result != null) {
    return result[1]
  }

  return name
}
