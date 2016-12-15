/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import download from './app/reducers/download'
import editable from './app/reducers/editable'
import exchange from './app/reducers/exchange'
import history from './app/reducers/history'
import reports from './reports/reducers/reports'
import drawer from './time-series/reducers/drawer'
import result from './time-series/reducers/result'
import suggestions from './time-series/reducers/suggestions'
import timeSeries from './time-series/reducers/timeSeries'
import timeWindow from './time-series/reducers/timeWindow'
import agents from './agents/reducers/agents'

export default {
  agents,
  download,
  editable,
  exchange,
  drawer,
  history,
  reports,
  result,
  suggestions,
  timeSeries,
  timeWindow,
}
