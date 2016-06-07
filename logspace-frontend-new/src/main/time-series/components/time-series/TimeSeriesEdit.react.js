/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React, {PropTypes} from 'react'

import TimeSeriesLabel from './TimeSeriesLabel.react'
import TimeSeriesEditScale from './TimeSeriesEditScale.react'

import {COLORS} from '../../actions/timeSeries.constants'

require('./TimeSeriesEdit.styl')

export default class TimeSeriesEdit extends React.Component {

  handleChange(event) {
    this.props.changeTimeseriesProperty(event.target.name, event.target.value)
  }

  render() {
    const agentDescription = this.props.editedTimeSeries

    const usedColors = this.props.timeSeries.map((item) => {
      if (item.get('id') === agentDescription.get('id')) {
        return ''
      }

      return item.get('color')
    })

    return (
      <div className="time-series-edit">
        <div className="time-series-label-wrapper">
          <TimeSeriesLabel timeSeries={agentDescription} />
        </div>

        <div className="details">
          <b>Select property</b>
          <br />
          {agentDescription.get('propertyDescriptions').map((property) => {
            const propertyId = property.get('id')
            return (
              <div key={propertyId}>
                <input
                  type="radio"
                  name="propertyId"
                  value={propertyId}
                  checked={propertyId === agentDescription.get('propertyId')}
                  onChange={(event) => this.handleChange(event)}
                />
                <nbsp />
                <span> {property.get('name')} </span>
                <br />
              </div>
            )
          })}

          <br />
          <b>Select aggregation </b>
          <br />
          <select name="aggregate" value={agentDescription.get('aggregate')} onChange={(event => this.handleChange(event))}>
            <option value="count">count</option>
            <option value="max">max</option>
            <option value="min">min</option>
            <option value="avg">average</option>
            <option value="sum">sum</option>
          </select>
          <br />
          <br />

          <b>Select color </b>
          <br />
          {COLORS.map((color) => {
            const colorSyle = {
              backgroundColor: color
            }
            return (
              <div key={color} className="color-option" style={colorSyle} >
                <input
                  type="radio"
                  name="color"
                  value={color}
                  checked={color === agentDescription.get('color')}
                  onChange={(event) => this.handleChange(event)}
                  disabled={color !== agentDescription.get('color') && usedColors.indexOf(color) > -1}
                />
              </div>
            )
          })}
          <div className="clearer" />
          <br />
          <TimeSeriesEditScale {...this.props} />

          <div className="buttons">
            <button className="waves-effect waves-light btn" onClick={() => this.props.saveTimeSeries()}>Save time series</button>
            <button
              className={(agentDescription.get('id') !== null) ? 'delete-visible waves-effect waves-light btn btn-highlight' : 'delete-hidden'}
              onClick={() => this.props.deleteTimeSeries(agentDescription.get('id'))}
            >
              Delete time series
            </button>
          </div>
        </div>
      </div>
    )
  }
}

TimeSeriesEdit.propTypes = {
  saveTimeSeries: PropTypes.func.isRequired,
  deleteTimeSeries: PropTypes.func.isRequired,
  editedTimeSeries: PropTypes.object.isRequired,
  changeTimeseriesProperty: PropTypes.func.isRequired,
  timeSeries: PropTypes.array.isRequired,
}
