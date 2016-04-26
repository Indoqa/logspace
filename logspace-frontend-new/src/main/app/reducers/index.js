import {combineReducers} from 'redux'

import timeReducers from '../../time/reducers'
import todoReducers from '../../todos/reducers'

const reducers = {
  ...timeReducers,
  ...todoReducers
}

export default combineReducers(reducers)
