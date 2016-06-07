/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React, {PropTypes} from 'react'
import classnames from 'classnames'

import TimeSeriesLabel from './TimeSeriesLabel.react'
import {isSubitem} from '../../actions/timeSeries.constants'

require('./TimeSeriesItem.styl')

function createTimeSeriesClassName(item) {
  return classnames('time-series-item', {
    subentry: isSubitem(item.get('scaleType')),
    mainentry: !isSubitem(item.get('scaleType'))
  })
}

export function cleanPropertyName(name) {
  const pattern = /[\w]*?_[\w]*?_(.*)/
  const result = pattern.exec(name)

  if (result !== null) {
    return result[1]
  }

  return name
}

export default class TimeSeriesItem extends React.Component {

  getAxisLabel(item) {
    if (isSubitem(item.get('scaleType'))) {
      return <span />
    }

    const classNames = `axis axis-${this.props.axis}`
    return <div className={classNames} title={this.getAxisTooltip()} />
  }

  getAxisTooltip() {
    if (this.props.axis === 1) {
      return 'Scale is shown on left y axis'
    }

    if (this.props.axis === 2) {
      return 'Scale is shown on right y axis'
    }

    return ''
  }

  render() {
    const bgStyle = {
      backgroundColor: this.props.item.get('color')
    }
    const item = this.props.item

    const axisLabel = this.getAxisLabel(item)

    return (
      <div className={createTimeSeriesClassName(item)} onClick={() => this.props.editTimeSeries(item)}>
        <div className="color" style={bgStyle}></div>
        <div className="inner">
          {axisLabel}
          <TimeSeriesLabel timeSeries={item} />
          <span className="property">{item.get('aggregate')} of {cleanPropertyName(item.get('propertyId'))}</span>
        </div>
      </div>
    )
  }
}

TimeSeriesItem.propTypes = {
  axis: PropTypes.string,
  item: PropTypes.object.isRequired,
  editTimeSeries: PropTypes.func.isRequired,
  cleanPropertyName: PropTypes.func.isRequired
}
