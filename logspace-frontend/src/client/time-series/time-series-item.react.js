/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react';
import PureComponent from '../components/purecomponent.react';
import {onTimeSeriesDeleted, onEditTimeSeries} from './actions';

require('./time-series-item.styl')

export default class TimeSeriesItem extends PureComponent {

  deleteTimeSeries() {
    onTimeSeriesDeleted(this.props.item);
  }

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
      <div className='time-series-item'>
        <div className='color' style={bgStyle}></div>
        <div className='inner'>
          {this.props.item.get("agentId")}<br/>
          {this.props.item.get("aggregate")} of {this.getPropertyName()}
          <br/>
          <button onClick={() => onEditTimeSeries(this.props.item)}>edit</button>
          <button onClick={() => this.deleteTimeSeries()}>delete</button>
        </div>
      </div>
    )
  }
}
