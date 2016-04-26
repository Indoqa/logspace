/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react';
import immutable from 'immutable';
import Component from '../components/component.react';
import TimeSeriesItem  from '../time-series/time-series-item.react'

import {isSubitem, getReference} from './constants';

export default class TimeSeriesList extends Component {
  positionMap(items) {
    const map = []

    items.forEach(function(item, index) {
      map[item.get('id')] = index
    })

    return map
  }

  getSortValue(item, index, positionMap) {
    const scaleType = item.get('scaleType') 

    if (!isSubitem(scaleType)) {
      return index + '-0-master'
    }

    const reference = getReference(scaleType)
    const indexOfReference = positionMap[reference]

    return indexOfReference + '-' + index + '-subitem'
  }

  render() {
    const items = this.props.items
    const positionMap = this.positionMap(items)
    const sortedItems = items.sortBy((item, index) => this.getSortValue(item, index, positionMap))

    let masterCount = 0

    return (
      <div className='time-series-list'>
        {sortedItems.map(function(item) {
          if (!isSubitem(item.get('scaleType')) && masterCount < 2) {
            masterCount++
            return <TimeSeriesItem key={item.get("id")} item={item} axis={masterCount}/>;
          }

          return <TimeSeriesItem key={item.get("id")} item={item} />;
        })}
      </div>
    )
  }
}

TimeSeriesList.propTypes = {
  items: React.PropTypes.instanceOf(immutable.List)
};
