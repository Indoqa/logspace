import {combineReducers} from 'redux'

import download from './download'
import editable from './editable'
import exchange from './exchange'
import history from './history'
import drawer from '../../time-series/reducers/drawer'
import result from '../../time-series/reducers/result'
import suggestions from '../../time-series/reducers/suggestions'
import timeSeries from '../../time-series/reducers/timeSeries'
import timeWindow from '../../time-series/reducers/timeWindow'

const reducers = {
  download,
  editable,
  exchange,
  drawer,
  history,
  result,
  suggestions,
  timeSeries,
  timeWindow
}

export default combineReducers(reducers)
