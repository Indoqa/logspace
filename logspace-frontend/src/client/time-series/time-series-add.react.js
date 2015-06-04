/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react';
import Component from '../components/component.react';

import {onShowSuggestions} from '../suggestions/actions'


export default class AddTimeSeries extends Component {

  render() {
    if (this.props.count >= 8) {
      return <div/>
    }

    return (
      <div className='add-series-entry'>
        <button className='btn-floating btn-large waves-effect btn-highlight' onClick={() => onShowSuggestions()}>
          <i>+</i>
        </button>
      </div>
    )
  }

}
