import React from 'react'
import Buttons from './Buttons.redux'
import Result from './Result.redux'

class TimePage extends React.Component {
  render() {
    return (
      <div>
        <Buttons />
        <br />
        <Result />
      </div>
    )
  }
}

export default TimePage

// Note: top level route components NEED to be react classes and can't be written as functional components!
// Otherwise hot reloading won't work: https://github.com/gaearon/react-hot-loader/issues/212
