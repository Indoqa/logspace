/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'

import {selections} from '../../actions/time-window.constants'

require('./time-window.styl')

export default class TimeWindowShortcuts extends React.Component {
  render() {
    return (
      <div className="shortcuts">
        <ul>
          {selections.map((selection) => {
            return (
              <li>
                <a onClick={() => this.props.selectPredefinedDate(selection)}>{selection.label}</a>
              </li>
            )
          })}
        </ul>
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
      </div>
    )
  }
}

TimeWindowShortcuts.propTypes = {
  selectPredefinedDate: PropTypes.func.isRequired,
}
