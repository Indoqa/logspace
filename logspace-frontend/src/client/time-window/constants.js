/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import moment from 'moment'
import {Record} from 'immutable'

export const GAPS = {
    second: 1,
    minute: 60,
    hour: 3600,
    day: 86400,
    week: 604800,
    month: 2629740,
    year: 31556900
}

export const TimeWindowSelection = Record({
  label: '',
  start: () => moment().utc().startOf('day'), 
  end: () => moment().utc().startOf('day'),
  gap: GAPS.hour
})

export const selections = [
  new TimeWindowSelection({
    label: 'Today',
    start: () => moment().startOf('day'), 
    end: () => moment().endOf('day'),
    gap: GAPS.hour
  }),
  new TimeWindowSelection({
    label: 'Yesterday',
    start: () => moment().subtract(1, 'days').startOf('day'), 
    end: () => moment().subtract(1, 'days').endOf('day'),
    gap: GAPS.hour
  }),
  new TimeWindowSelection({
    label: 'current hour',
    start: () => moment().startOf('hour'), 
    end: () => moment().endOf('hour'),
    gap: GAPS.hour
  }),
  new TimeWindowSelection({
    label: 'previous hour',
    start: () => moment().subtract(1, 'hours').startOf('hour'), 
    end: () => moment().subtract(1, 'hours').endOf('hour'),
    gap: GAPS.hour
  }),
  new TimeWindowSelection({
    label: 'last 60 minutes',
    start: () => moment().subtract(60, 'minutes'), 
    end: () => moment(),
    gap: GAPS.hour
  })
]

