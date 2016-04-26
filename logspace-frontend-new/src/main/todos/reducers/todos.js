import {List, fromJS} from 'immutable'

const initialState = new List

const todos = (state = initialState, action) => {
  switch (action.type) {
    case 'ADD_TODO':
      return state.push(fromJS({
        id: action.id,
        text: action.text,
        completed: false
      }))
    case 'TOGGLE_TODO':
      return state.update(
        state.findIndex(item => item.get('id') === action.id),
        item => item.set('completed', !item.get('completed'))
      )
    default:
      return state
  }
}

export default todos
