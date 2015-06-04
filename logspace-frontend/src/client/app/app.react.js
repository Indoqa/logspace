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

import * as appState from '../state'

import '../intl/store'
import '../time-window/store'
import '../time-series/store'
import '../result/store'
import '../suggestions/store'
import '../drawer/store'

require('./app.styl');

export default class App extends React.Component {

  constructor(props) {
    super(props)
    this.state = this.getState()
  }

  getState() {
    return {
      i18n: appState.i18nCursor(),
      timeWindow: appState.timeWindowCursor(),
      timeSeries: appState.timeSeriesCursor(),
      editedTimeSeries: appState.editedTimeSeriesCursor(),
      result: appState.resultCursor().get('translatedResult'),
      suggestions: appState.suggestionCursor(),
      activePanel: appState.drawerCursor().get('activePanel')
    };
  }

  componentDidMount() {
    appState.state.on('change', () => {
      console.time('app render') // eslint-disable-line no-console
      this.setState(this.getState(), () => {
        console.timeEnd('app render') // eslint-disable-line no-console
      })
    })
  }

  render() {
    return (
      <DocumentTitle title='logspace.io'>
        <div className="page">
          <RouteHandler {...this.state} />
        </div>
      </DocumentTitle>
    )
  }

}
