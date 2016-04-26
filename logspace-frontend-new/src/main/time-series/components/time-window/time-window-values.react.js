/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React, {PropTypes} from 'react'

require('./time-window-values.styl')

export default class TimeWindowValues extends React.Component {

  render() {
    const {timeWindow, showTimeWindowForm} = this.props

    return (
      <div className="time-window-values" onClick={() => showTimeWindowForm()}>
        <span className="gap">{timeWindow.get('gap').get('amount')} {timeWindow.get('gap').get('unit').get('short')}</span>
        <span dangerouslySetInnerHTML={{__html: timeWindow.get('label')}} />
      </div>
    )
  }
}

TimeWindowValues.propTypes = {
  timeWindow: PropTypes.object,
  showTimeWindowForm: PropTypes.func.isRequired
}
