import React from 'react'
import AddTodo from '../components/AddTodo.redux'
import Footer from '../components/Footer.react'
import VisibleTodoList from '../components/TodoList.redux'

import './Page.styl'

class TodoPage extends React.Component {
  render() {
    return (
      <div>
        <AddTodo />
        <VisibleTodoList />
        <Footer />
      </div>
    )
  }
}

export default TodoPage

// Note: top level route components NEED to be react classes and can't be written as functional components!
// Otherwise hot reloading won't work: https://github.com/gaearon/react-hot-loader/issues/212

