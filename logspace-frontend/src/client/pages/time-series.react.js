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
import TimeSeriesList  from '../time-series/time-series-list.react.js'
import Chart  from '../result/result-chart.react.js'
import Drawer  from '../drawer/drawer.react.js'
import Header  from '../header/header.react.js'
import TimeWindowValues  from '../time-window/time-window-values.react.js'
import {getTimeWindow} from '../time-window/store';
import {getTimeSeries, getEditedTimeSeries} from '../time-series/store';
import {getActivePanel} from '../drawer/store';
import {getResult} from '../result/store';
import {getSuggestions} from '../suggestions/store';
import {onShowSuggestions} from '../suggestions/actions';
import {onShowTimeWindowForm} from '../time-window/actions';

require('./time-series.styl')

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
    this.forceUpdate()
  }

  render() {
    var timeWindow = getTimeWindow();

    return (
      <div className='time-series'>
        <Header />

        <div className={classnames(this.state.navDrawerCss)}>
          <div className="left">
            <TimeWindowValues />
            <TimeSeriesList items={getTimeSeries()} />

            <div className='add-series-entry'>
              <button className='btn-floating btn-large waves-effect btn-highlight' onClick={() => onShowSuggestions()}>
                <i>+</i>
              </button>
            </div>

            <div className='tools'>
              Tools
            </div>

          </div>
          <div className="right">
            <Drawer 
              activePanel={getActivePanel()} 
              suggestions={getSuggestions()} 
              timeWindow={getTimeWindow()} 
              editedTimeSeries={getEditedTimeSeries()}
              toggle={() => this.toggleNavigationDrawer()} />
          </div>
        </div>

        <div className={classnames(this.state.mainCss)}>
          <Chart series={getTimeSeries()} result={getResult()}/>
        </div>
      </div>
    )
  }
}
