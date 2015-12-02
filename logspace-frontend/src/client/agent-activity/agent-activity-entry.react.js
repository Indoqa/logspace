/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
 import React from 'react';
 import immutable from 'immutable';
 import Component from '../components/component.react';

 import c3 from 'c3'
 import {getRandomString} from '../../lib/getrandomstring';

 import debounce from '../../lib/debounce'

 export default class AgentActivityEntry extends Component {

   constructor(props) {
     super(props);
   }

   updateChartData() {
     this.chartElement.style.opacity = '1'

     const agentActivity = this.props.agentActivity;
     var history = this.props.agentActivity.get('history').toJS();
     history.unshift('Event Count')

     this.chart.load({
       columns: [
         history
       ]
    })

    var maxHistoryValue = this.props.maxHistoryValue
    this.chart.axis.max({
      y: maxHistoryValue
    })
   }

   updateChart() {
     if (!this.chart) {
       this.chart = c3.generate({
         bindto: '#' + this.chartId,
         data: {
           columns: [],
           type: 'bar'
         },
         bar: {
           width: 8
         },
         size: {
           width: 600,
           height: 50
         },
         padding: {
           top : 0,
           left : 0,
           bottom : 0,
           right : 0
         },
         color: {
           pattern: ['#f17f49']
         },
         transition: {
           duration: 500
         },
         legend: {
           show: false
         },
         axis: {
           x: {
             show: false
           },
           y: {
             show: false
           }
         }
       })

       this.chartElement = React.findDOMNode(this.refs[this.chartId])
     }

     this.chartElement.style.opacity = '0.25'

     setTimeout(() => {
       this.updateChartData()
     }, 50)
   }

   componentDidMount() {
     this.updateChart()
   }

   componentDidUpdate() {
     this.updateChart()
   }

   render() {
     const agentActivity = this.props.agentActivity;
     this.chartId = "chart-" + getRandomString();

     return (
       <tr>
         <td>{agentActivity.get('globalAgentId')}</td>
         <td>{agentActivity.get('eventCount')}</td>
         <td><div ref={this.chartId} id={this.chartId} /></td>
       </tr>
     )
   }
}

AgentActivityEntry.propTypes = {
  agentActivity: React.PropTypes.instanceOf(immutable.Map)
};
