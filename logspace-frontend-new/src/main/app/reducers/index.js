import {combineReducers} from 'redux'

import editable from './editable'
import drawer from '../../time-series/reducers/drawer'
import result from '../../time-series/reducers/result'
import suggestions from '../../time-series/reducers/suggestions'
import timeSeries from '../../time-series/reducers/time-series'
import timeWindow from '../../time-series/reducers/time-window'

const reducers = {
  editable,
  drawer,
  result,
  suggestions,
  timeSeries,
  timeWindow
}

export default combineReducers(reducers)
