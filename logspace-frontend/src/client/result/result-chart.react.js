/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react';
import PureComponent from '../components/purecomponent.react';

export default class Chart extends PureComponent {
  constructor(props) {
    super(props);
    
    this.state = {
      chartData: [],
      empty: true,
      loading: false
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.result == null || nextProps.result.get("data") == null 
      || nextProps.result.get("data").isEmpty() 
      || nextProps.series.size != nextProps.result.get("data").size) {
       this.setState({
        chartData: [],
        empty: true,
        loading: false
      });
      return; 
    }

    var chartData = [];
    var data = nextProps.result.get("data");
    var series = nextProps.series;

    for (var i = 0; i < data.size; i++) {
       var line = {
          color: series.get(i).get("color"),
          key: series.get(i).get("id"),
          data: data.get(i)
       }
       chartData.push(line);
     }

     this.setState({
        chartData: chartData,
        empty: false,
        loading: false
     });
  }


  render() {
    if (this.state.loading) {
       return <div>loading..</div>
    }

    if (this.state.empty) {
       return <div>no data</div>
    }

    return (
      <div>
        Chart: 
        {this.state.chartData.map(function(item, index) {
          var bgStyle = {
            backgroundColor: item.color,
            padding: '5px',
            marginLeft: '5px',
            width: '200px'
          }
          
          return (
            <div key={item.key} style={bgStyle}>
              {item.key}: {item.data} 
            </div>
            );
        })}
      </div>
    );
  }
}