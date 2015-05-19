/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import {Link} from 'react-router'
<<<<<<< HEAD
import classnames from 'classnames'

import AddTimeSerie from '../time-series/add-time-series.react'
<<<<<<< HEAD
import classnames from 'classnames';
=======
>>>>>>> 77617067d83ef5625733158f8c7c28232f6fc331
import TimeSeriesList  from '../time-series/time-series-list.react.js'
import Chart  from '../result/result-chart.react.js'
import Drawer  from '../drawer/drawer.react.js'
import {getTimeWindow} from '../time-window/store';
<<<<<<< HEAD
import {getTimeSeries, getEditedTimeSeries} from '../time-series/store';
=======
import {getTimeSeries} from '../time-series/store';
>>>>>>> 77617067d83ef5625733158f8c7c28232f6fc331
import {getActivePanel} from '../drawer/store';
import {getResult} from '../result/store';
import {getSuggestions} from '../suggestions/store';
import {onShowSuggestions} from '../suggestions/actions';
import {onShowTimeWindowForm} from '../time-window/actions';
<<<<<<< HEAD

require('./time-series.styl')
=======
>>>>>>> 77617067d83ef5625733158f8c7c28232f6fc331

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
<<<<<<< HEAD
            <TimeWindowValues />
=======
            <div> {timeWindow.get('start')} </div>
            <div> {timeWindow.get('end')} </div>
            <div> {timeWindow.get('gap')} </div>
            <input type="button" value="change time" onClick={() => onShowTimeWindowForm()} />  
            <hr/>
            <button onClick={() => onShowSuggestions()}>+</button>
            <hr/>
>>>>>>> 77617067d83ef5625733158f8c7c28232f6fc331
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
<<<<<<< HEAD
              editedTimeSeries={getEditedTimeSeries()}
              toggle={() => this.toggleNavigationDrawer()} />
          </div>
=======
              toggle={() => this.toggleNavigationDrawer()} />
          </div>  
>>>>>>> 77617067d83ef5625733158f8c7c28232f6fc331
        </div>

        <div className={classnames(this.state.mainCss)}>
          <Chart series={getTimeSeries()} result={getResult()}/>
        </div>
      </div>
    )
  }

}
