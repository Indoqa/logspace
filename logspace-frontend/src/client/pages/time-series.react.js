/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import {Link} from 'react-router'
import classnames from 'classnames';
import AddTimeSerie from '../time-series-selection/addtimeserie.react'
import TimeWindow  from '../time-window/time-window.react.js'
import {getTimeWindow} from '../time-window/store';

export default class TimeSeries extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      navDrawerCss: 'navigation-drawer',
      mainCss: 'main'
    };
  }

  toggleNavigationDrawer() {
    this.setState(
      {
        navDrawerCss: {
          'navigation-drawer' : true,
          'navigation-drawer-expanded' : !this.state.navDrawerCss['navigation-drawer-expanded']
        },
        mainCss: {
          'main' : true,
          'main-reduced' : !this.state.mainCss['main-reduced']
        }
      });
    this.forceUpdate();
  }

  render() {
    const t = getTimeWindow();
    return (
      <div className='time-series'>
        <div className='header'>
          logspace.io <button onClick={() => this.toggleNavigationDrawer()}>Toggle navigation drawer</button>
        </div>

        <div className={classnames(this.state.navDrawerCss)}>
          <TimeWindow timeWindow={t} />
          <AddTimeSerie />
          <div className='tools'>
            Tools
          </div>
        </div>

        <div className={classnames(this.state.mainCss)}>
          <div className='graph'>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph<br/>
            graph (last)<br/>
          </div>
        </div>
      </div>
    )
  }

}
