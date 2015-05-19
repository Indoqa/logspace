/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react';
import PureComponent from '../components/purecomponent.react';
import Suggestions  from '../suggestions/suggestions.react.js'
import TimeWindow  from '../time-window/time-window.react.js'
import EditTimeSeries from '../time-series/time-series-edit.react'
import * as Panels from './constants';

require('./drawer.styl')

export default class Drawer extends PureComponent {

  componentDidUpdate(prevProps, prevState) {
  	if (prevProps.activePanel == this.props.activePanel ) {
  		return;
  	}

  	if (prevProps.activePanel == null || this.props.activePanel == null) {
  		this.props.toggle(); 	
  	}
  }

  render() {
  	if (this.props.activePanel == null) {
  		return <div/>
  	}

  	if (this.props.activePanel == Panels.SUGGESTIONS) {
  		return (
    		<Suggestions suggestions={this.props.suggestions}/>  
    	);
  	}

  	if (this.props.activePanel == Panels.TIME_WINDOW) {
  		return (
    		<TimeWindow timeWindow={this.props.timeWindow} />
    	);
  	}

    if (this.props.activePanel == Panels.TIMESERIES) {
      return (
        <EditTimeSeries editedTimeSeries={this.props.editedTimeSeries} />
      );
    }
    

  	return (<div> unsupported panel: {this.props.activePanel} </div>)
  }
}