import {fromJS} from 'immutable'
import {FETCH_TIME, CLEAR_TIME} from '../actions/time'

const initiatState = fromJS({
  result: null,
  error: null,
  isLoading: false
})

const time = (state = initiatState, action) => {
  switch (action.type) {
    case `${FETCH_TIME}_START`:
      return state.set('isLoading', true)

    case `${FETCH_TIME}_SUCCESS`:
      state = state.set('isLoading', false)
      state = state.set('result', fromJS(action.payload))
      state = state.set('error', null)
      return state

    case `${FETCH_TIME}_ERROR`:
      state = state.set('isLoading', false)
      state = state.set('result', null)
      state = state.set('error', action.payload.statusText)
      return state

    case CLEAR_TIME:
      state = state.set('result', null)
      state = state.set('error', null)
      return state

    default:
      return state
  }
}

export default time
