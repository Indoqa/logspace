/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react';
import PureComponent from '../components/purecomponent.react';

require('./time-series-label.styl')

export default class TimeSeriesLabel extends PureComponent {

  render() {
    return (
      <div className='time-series-label'>
        <div className={'meta'}>
          <span> {this.props.timeSeries.get("space")} </span>
          <span> {this.props.timeSeries.get("system")} </span>
        </div>
        <div className={'name'}>
          <span> {this.props.timeSeries.get("name")} </span>    
        </div>
      </div>
    )
  }
}
