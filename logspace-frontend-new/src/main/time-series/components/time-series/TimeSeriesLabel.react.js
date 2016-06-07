/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React, {PropTypes} from 'react'

import './TimeSeriesLabel.styl'

export default class TimeSeriesLabel extends React.Component {

  render() {
    return (
      <div className="time-series-label">
        <div className={'meta'}>
          <span className="space">{this.props.timeSeries.get('space')}</span>
          <span className="system">{this.props.timeSeries.get('system')}</span>
        </div>
        <div className={"name"}>
          <span>{this.props.timeSeries.get('name')}</span>
        </div>
      </div>
    )
  }
}

TimeSeriesLabel.propTypes = {
  timeSeries: PropTypes.array.isRequired,
}
