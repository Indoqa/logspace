/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react';
import Component from '../components/component.react';
import TimeSeriesLabel from './time-series-label.react';
import {onTimeSeriesDeleted, onEditTimeSeries} from './actions';

require('./time-series-item.styl')

export default class TimeSeriesItem extends Component {

  getPropertyName() {
    const id = this.props.item.get("propertyId");
    const pattern = /[\w]*?_[\w]*?_(.*)/
    const result = pattern.exec(id)

    if(result != null) {
      return result[1]
    }
    return id
  }

  render() {
    const bgStyle = {
      backgroundColor: this.props.item.get("color")
    }

    return (
      <div className='time-series-item' onClick={() => onEditTimeSeries(this.props.item)}>
        <div className='color' style={bgStyle}></div>
        <div className='inner'>
          <div className='axis'>  {this.props.item.get("axis")} </div>
          <TimeSeriesLabel timeSeries={this.props.item} />
          <span className='property'>{this.props.item.get("aggregate")} of {this.getPropertyName()}</span>
        </div>
      </div>
    )
  }
}
