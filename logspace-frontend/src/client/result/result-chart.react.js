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
import c3 from 'c3'

require ('./result-chart.css')

export default class Chart extends PureComponent {

  componentDidMount() {
    this.chart = c3.generate(this.chartOptions());
  }

  componentWillUnmount() {
    this.chart.destroy();
  }

  componentDidUpdate() {
    if (this.props.result.get("empty") || this.props.result.get("error")) {
      this.chart.unload()
      return;
    }

    var chartData = this.props.result.get("chartData").toJS();
    var currentData = this.chart.data();

    var keysToUnload = currentData.map(function(item) {
      if (chartData.columnKeys.indexOf(item.id) > -1 ) {
        return null;  
      } 
      return item.id;
    }); 

    this.chart.load(
      {
          colors: chartData.colors,
          columns: chartData.columns,
          unload: keysToUnload
      }  
    );

    this.chart.xgrids([
        {value: 0, text:chartData.columns[0][1]},
        {value: chartData.columns[0].length - 2, text: chartData.columns[0][chartData.columns[0].length - 1]}
      ]);
    }  

  transform(type) {
    this.chart.transform(type);
  }

  render() {
    return (
        <div>
          <div id="chart"  / >
          <br/>
          <button onClick={() => this.transform('bar')}>bar</button>
          <button onClick={() => this.transform('line')}>line</button>
          <button onClick={() => this.transform('spline')}>spline</button>
          <button onClick={() => this.transform('step')}>step</button>
          <button onClick={() => this.transform('area')}>area</button>
          <button onClick={() => this.transform('area-spline')}>area-spline</button>
          <button onClick={() => this.transform('area-step')}>area-step</button>
          <button onClick={() => this.transform('scatter')}>scatter</button>
          <button onClick={() => this.transform('pie')}>pie</button>
          <button onClick={() => this.transform('donut')}>donut</button>
          <button onClick={() => this.transform('gauge')}>gauge</button>
        </div>
    );
  }

  chartOptions() {
    return {
      data: {
          x: 'x',
          columns: [          ],        
        },
      axis: {
        x: {
            type: 'category',
            tick: {
              culling: true
            }
        },
         y: {
            show: false
          }
        },
      grid: {
        x: {
          show: false
        },
        y: {
          show: false
        }
      },
      legend: {
        show: false
      },
      point: {
         r: 2
      },
      size: {
        height: 680
      }
    }
  }
}