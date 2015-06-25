/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react'
import Component from '../components/component.react'

import {onShowTimeWindowForm} from './actions';

require('./time-window-values.styl')

export default class TimeWindowValues extends Component {

  render() {
    return (
      <div className='time-window-values' onClick={() => onShowTimeWindowForm()}>
        <span className='gap'>{this.props.timeWindow.get('gap').get('amount')} {this.props.timeWindow.get('gap').get('unit').get('short')}</span>
        <span dangerouslySetInnerHTML={{__html: this.props.timeWindow.get('label')}} />
      </div>
    )
  }

}
