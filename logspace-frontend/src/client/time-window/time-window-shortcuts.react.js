/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
 
import React from 'react';
import Immutable from 'immutable'
import Component from '../components/component.react'
import DateRangePicker from 'react-daterange-picker'
import Tabs from 'react-simpletabs'
import moment from 'moment'

import {selectCustomDate, selectPredefinedDate, selectDynamicDate} from './actions';
import {GAPS,selections} from './constants'

require('./time-window.styl')

export default class TimeWindowShortcuts extends Component {
  render() {
    return (
      <div>
        <div>
          {selections.map(function(selection) {
            return (
              <div>
                <a onClick={() => selectPredefinedDate(selection)}>{selection.label}</a>
              </div>
            );
          })}
        </div>
     
      </div>
    )
  }

}
