/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React, {PropTypes} from 'react'
import immutable from 'immutable'
import TimeSeriesItem from '../time-series/time-series-item.react'
import {isSubitem, getReference} from '../../actions/time-series.constants'

export default class TimeSeriesList extends React.Component {
  getSortValue(item, index, positionMap) {
    const scaleType = item.get('scaleType')


    if (!isSubitem(scaleType)) {
      return `${index}-0-master`
    }

    const reference = getReference(scaleType)
    const indexOfReference = positionMap[reference]

    return `${indexOfReference}-${index}-subitem`
  }

  positionMap(items) {
    const map = []

    items.forEach((item, index) => {
      map[item.get('id')] = index
    })

    return map
  }

  render() {
    const items = this.props.items
    const positionMap = this.positionMap(items)

    const sortedItems = items.sortBy((item, index) => {
      const sortValue = this.getSortValue(item, index, positionMap)
      console.log(sortValue)
      return sortValue
    })

    let masterCount = 0

    return (
      <div className="time-series-list">
        {sortedItems.map((item) => {
          if (!isSubitem(item.get('scaleType')) && masterCount < 2) {
            masterCount++
            return (
              <TimeSeriesItem
                key={item.get('id')}
                item={item}
                axis={masterCount}
                editTimeSeries={this.props.editTimeSeries}
                cleanPropertyName={this.props.cleanPropertyName}
              />
            )
          }

          return (
            <TimeSeriesItem key={item.get('id')} item={item}
              key={item.get('id')}
              item={item}
              editTimeSeries={this.props.editTimeSeries}
              cleanPropertyName={this.props.cleanPropertyName}
            />
          )
        })}
      </div>
    )
  }
}

TimeSeriesList.propTypes = {
  items: PropTypes.instanceOf(immutable.List),
  editTimeSeries: PropTypes.func.isRequired,
  cleanPropertyName: PropTypes.func.isRequired
}
