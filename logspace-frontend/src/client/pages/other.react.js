/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import {Link} from 'react-router'
var request = require('superagent')

export default class Home extends React.Component {

  doSomething() {
    alert('New este.js project');
  }

  render() {
    return (
      <div>
        <p>Other</p>
        <button onClick={() => this.doSomething()}>Press me!</button>
      </div>
    )
  }

}
