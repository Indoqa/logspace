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
import AddTimeSerie from '../time-series/add-time-series.react'
import TimeWindow  from '../time-window/time-window.react.js'
import TimeSeriesList  from '../time-series/time-series-list.react.js'
import Suggestions  from '../suggestions/suggestions.react.js'
import Chart  from '../result/result-chart.react.js'
import {getTimeWindow} from '../time-window/store';
import {getTimeSeries} from '../time-series/store';
import {getResult} from '../result/store';
import {getSuggestions} from '../suggestions/store';

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
    return (
      <div className='time-series'>
        <div className='header'>
          logspace.io
        </div>

        <div className={classnames(this.state.navDrawerCss)}>
          <div className="left">
            <TimeWindow timeWindow={getTimeWindow()} />
            <hr/>
            <button onClick={() => this.toggleNavigationDrawer()}>+</button>
            <hr/>
            <TimeSeriesList items={getTimeSeries()} />
            <div className='tools'>
              Tools
            </div>
          </div>
          <div className="right">
            <Suggestions suggestions={getSuggestions()}/>
          </div>  
        </div>

        <div className={classnames(this.state.mainCss)}>
          <Chart series={getTimeSeries()} result={getResult()}/>
        </div>
      </div>
    )
  }

}
