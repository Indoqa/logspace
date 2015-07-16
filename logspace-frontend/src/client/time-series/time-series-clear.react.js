/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react';
import Component from '../components/component.react';

import {reset} from '../time-series/actions'


export default class ClearTimeSeries extends Component {

  resetAll() {
    reset()  
    refreshResult()
  }

  render() {
    if (this.props.count == 0) {
      return <div/>
    }

    return (
      <span className='delete-all' onClick={() => this.resetAll()}>
        Delete all
      </span>
    )
  }

}
