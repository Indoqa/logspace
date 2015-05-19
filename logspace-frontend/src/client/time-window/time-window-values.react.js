/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react'
import PureComponent from '../components/purecomponent.react'

import {getTimeWindow} from './store'
import {onShowTimeWindowForm} from './actions';

require('./time-window-values.styl')

export default class TimeWindowValues extends PureComponent {

  render() {
    const timeWindow = getTimeWindow()

    return (
      <div className='time-window-values'>
        <div>from: {timeWindow.get('start')} </div>
        <div>to: {timeWindow.get('end')} </div>
        <div>gap: {timeWindow.get('gap')} seconds</div>
        <button onClick={() => onShowTimeWindowForm()}>change</button>
      </div>
    )
  }

}
