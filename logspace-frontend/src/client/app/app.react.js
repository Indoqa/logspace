/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import DocumentTitle from 'react-document-title'
import React from 'react'
import {Link, RouteHandler} from 'react-router'
import {state} from '../state'

require('./app.styl');

export default class App extends React.Component {

  componentDidMount() {
    state.on('change', () => {
      this.forceUpdate();
    })
  }

  render() {
    return (
      <DocumentTitle title='logspace.io'>
        <div className="page">
          <header>
            <h1>Welcome to logspace.io</h1>
            <ul>
              <li><Link to="home">Home</Link></li>
              <li><Link to="other">Other</Link></li>
            </ul>
          </header>
          <hr />
          <RouteHandler />
          <hr />
          <footer>
            <p>created by este-boot</p>
          </footer>
        </div>
      </DocumentTitle>
    )
  }

}
