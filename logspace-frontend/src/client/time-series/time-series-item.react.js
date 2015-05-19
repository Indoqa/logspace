/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react';
import PureComponent from '../components/purecomponent.react';
import {onTimeSeriesDeleted} from './actions';

require('./time-series-item.styl')

export default class TimeSeriesItem extends PureComponent {

  deleteTimeSeries() {
    onTimeSeriesDeleted(this.props.item);
  }

  render() {
    var bgStyle = {
      backgroundColor: this.props.item.get("color")
    }

    return (
      <div className='time-series-item'>
        <div className='color' style={bgStyle}></div>
        <div className='inner'>
          {this.props.item.get("id")}<br/>
          {this.props.item.get("agentId")}<br/>
          {this.props.item.get("propertyId")}<br/>
          {this.props.item.get("aggregate")}<br/>
          <button onClick={() => this.deleteTimeSeries()}>delete</button>
        </div>
      </div>
    )
  }
}
