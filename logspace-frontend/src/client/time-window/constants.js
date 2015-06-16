/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import moment from 'moment'
import {Record} from 'immutable'
import Immutable from 'immutable'

export const units = Immutable.fromJS({
    second: {
      id: 1,
      label: 'second(s)',
      short: 's',
      factor: 1
    },
    minute: {
      id: 2,
      label: 'minute(s)',
      short: 'min',
      factor: 60
    },
    hour: {
      id: 3,
      label: 'hours(s)',
      short: 'h',
      factor: 3600
    },
    day: {
      id: 4,
      label: 'days(s)',
      short: 'd',
      factor: 86400
    },
    week: {
      id: 5,
      label: 'week(s)',
      short: 'w',
      factor: 604800
    },
    month: {
      id: 6,
      label: 'month(s)',
      short: 'mon',
      factor: 2629740
    },
    year: {
      id: 7,
      label: 'year(s)',
      short: 'y',
      factor: 31556900
    }
})

export const TimeWindowSelection = Record({
  label: '',
  start: () => moment().utc().startOf('day'), 
  end: () => moment().utc().startOf('day'),
  dynamicDuration: 60,
  dynamicUnit: units.get('second'),
  gap: Immutable.fromJS({
    amount: 1,
    unit: units.get('hour')
  })
})

export const selections = [
  new TimeWindowSelection({
    label: 'Today',
    start: () => moment().startOf('day'), 
    end: () => moment().endOf('day'),
    gap: Immutable.fromJS({
      amount: 1,
      unit: units.get('hour')
    })
  }),
  new TimeWindowSelection({
    label: 'Yesterday',
    start: () => moment().subtract(1, 'days').startOf('day'), 
    end: () => moment().subtract(1, 'days').endOf('day'),
    gap: Immutable.fromJS({
      amount: 1,
      unit: units.get('hour')
    })
  }),
  new TimeWindowSelection({
    label: 'current hour',
    start: () => moment().startOf('hour'), 
    end: () => moment().endOf('hour'),
    gap: Immutable.fromJS({
      amount: 1,
      unit: units.get('minute')
    })
  }),
  new TimeWindowSelection({
    label: 'previous hour',
    start: () => moment().subtract(1, 'hours').startOf('hour'), 
    end: () => moment().subtract(1, 'hours').endOf('hour'),
    gap: Immutable.fromJS({
      amount: 1,
      unit: units.get('minute')
    })
  }),
  new TimeWindowSelection({
    label: 'last 60 minutes',
    start: () => moment().subtract(60, 'minutes'), 
    end: () => moment(),
    gap: Immutable.fromJS({
      amount: 1,
      unit: units.get('minute')
    })
  })
]

