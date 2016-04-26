import expect from 'expect'
import {fromJS} from 'immutable'

import todos from '../../../main/todos/reducers/todos'

describe('todos reducer', () => {
  it('should handle initial state', () => {
    expect(
      todos(undefined, {}).toJS()
    ).toEqual([])
  })

  it('should handle ADD_TODO', () => {
    expect(
      todos(fromJS([]), {
        type: 'ADD_TODO',
        text: 'Run the tests',
        id: 0
      }).toJS()
    ).toEqual([
      {
        text: 'Run the tests',
        completed: false,
        id: 0
      }
    ])

    expect(
      todos(fromJS([
        {
          text: 'Run the tests',
          completed: false,
          id: 0
        }
      ]), {
        type: 'ADD_TODO',
        text: 'Use Redux',
        id: 1
      }).toJS()
    ).toEqual([
      {
        text: 'Run the tests',
        completed: false,
        id: 0
      }, {
        text: 'Use Redux',
        completed: false,
        id: 1
      }
    ])

    expect(
      todos(fromJS([
        {
          text: 'Run the tests',
          completed: false,
          id: 0
        }, {
          text: 'Use Redux',
          completed: false,
          id: 1
        }
      ]), {
        type: 'ADD_TODO',
        text: 'Fix the tests',
        id: 2
      }).toJS()
    ).toEqual([
      {
        text: 'Run the tests',
        completed: false,
        id: 0
      }, {
        text: 'Use Redux',
        completed: false,
        id: 1
      }, {
        text: 'Fix the tests',
        completed: false,
        id: 2
      }
    ])
  })

  it('should handle TOGGLE_TODO', () => {
    expect(
      todos(fromJS([
        {
          text: 'Run the tests',
          completed: false,
          id: 1
        }, {
          text: 'Use Redux',
          completed: false,
          id: 0
        }
      ]), {
        type: 'TOGGLE_TODO',
        id: 1
      }).toJS()
    ).toEqual([
      {
        text: 'Run the tests',
        completed: true,
        id: 1
      }, {
        text: 'Use Redux',
        completed: false,
        id: 0
      }
    ])
  })

})
