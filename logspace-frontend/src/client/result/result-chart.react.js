/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react';
import PureComponent from '../components/purecomponent.react';
import {onResultRefreshed} from './actions';
import ChartJS from 'react-chartjs'

export const LineChart = ChartJS.Line;

export default class Chart extends PureComponent {

  constructor(props) {
    super(props);
    
    this.state = {
      options:  {
        responsive: false,
        maintainAspectRatio: false
      },
      chartData: null
    }
  }

  render() {
    const result = this.props.result;

    if (result.get("error") == true) {
       return (
          <div>
            Logspace REST Error {result.get('errorStatus')}: {result.get('errorText')}
            <button onClick={onResultRefreshed}>Try again</button>
          </div>
        )
    }

    if (result.get("empty") == true) {
       return <div>Please add a time series</div>
    }

    return (
      <div>
        <LineChart data={result.get("chartData").toJS()} options={this.state.options} width="600" height="500" redraw/>
      </div>
    );
  }
}