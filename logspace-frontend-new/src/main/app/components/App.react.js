/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import {Div} from 'indoqa-rebass-components'

import './App.styl'

class App extends React.Component {

  getChildContext() {
    return {
      location: this.props.location
    }
  }

  render() {
    const {children} = this.props

    return (
      <Div>
        {children}
      </Div>
    )
  }
}

App.propTypes = {
  children: PropTypes.node.isRequired,
  location: PropTypes.object.isRequired,
}

App.childContextTypes = {
  location: PropTypes.object.isRequired,
}

export default App

// Note: top level route components NEED to be react classes and can't be written as functional components!
// Otherwise hot reloading won't work: https://github.com/gaearon/react-hot-loader/issues/212
