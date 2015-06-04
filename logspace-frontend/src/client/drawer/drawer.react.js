/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import Component from '../components/component.react'
import DrawerPanel  from './drawer-panel.react'
import {onCloseDrawer} from './actions'
import {msg} from '../intl/store'

require('./drawer.styl')

export default class Drawer extends Component {

  render() {
  	return (
      <div className="drawer">
        <div className="header">
          <div className="close"> <input type="button" value="close" onClick={() => onCloseDrawer()}/> </div>
          <div className="title"> > {msg('drawer.header.' + this.props.view.get('activePanel'))} </div>
        </div>
        <div className="panel">
          <DrawerPanel
              suggestions={this.props.suggestions}
              timeWindow={this.props.timeWindow}
              timeSeries={this.props.timeSeries}
              editedTimeSeries={this.props.editedTimeSeries}
              view={this.props.view} />
        </div>
      </div>
      )

  }
}
