/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import ReactDOM from 'react-dom'

import c3 from 'c3'

export default class AgentActivityEntry extends React.Component {

  componentDidMount() {
    this.createChart()
    this.updateChart()
  }

  componentDidUpdate() {
    this.updateChart()
  }

  updateChart() {
    const chartElement = ReactDOM.findDOMNode(this.refs[this.props.index])

    if (this.props.loading) {
      chartElement.style.opacity = '0.25'
    } else {
      chartElement.style.opacity = '1'

      const history = this.props.agentActivity.get('history').toJS()
      history.unshift('Event Count')

      this.chart.axis.max({y: this.props.maxHistoryValue})
      this.chart.load({columns: [history]})
    }
  }

  createChart() {
    this.chart = c3.generate({
      bindto: `#${this.props.index}`,
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
        top: 0,
        left: 0,
        bottom: 0,
        right: 0
      },
      color: {
        pattern: ['#f17f49']
      },
      transition: {
        duration: 0
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
  }

  render() {
    const {agentActivity} = this.props

    return (
     <tr key={this.props.index}>
       <td>{agentActivity.get('globalAgentId')}</td>
       <td>{agentActivity.get('eventCount')}</td>
       <td><div ref={this.props.index} id={this.props.index} /></td>
     </tr>
    )
  }
}

AgentActivityEntry.propTypes = {
  agentActivity: React.PropTypes.object.isRequired,
  maxHistoryValue: React.PropTypes.number.isRequired,
  loading: React.PropTypes.bool.isRequired,
  index: React.PropTypes.string.isRequired,
}
