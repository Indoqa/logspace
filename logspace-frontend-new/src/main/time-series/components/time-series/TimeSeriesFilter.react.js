/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React, {PropTypes} from 'react'
import Select from 'react-select'

import './TimeSeriesFilter.styl'


export default class TimeSeriesFilter extends React.Component {

  filterOptions(options) {
    return options
  }

  renderOption(option) {
    return (<span className={'option'}>{option.label}</span>)
  }

  renderValue(option) {
    return (<span>
      <span style={{fontSize: '100%'}}>
      {option.label}:
      </span>
      <span style={{fontSize: '100%', paddingLeft: '5px'}}>
        {option.input}
      </span>
    </span>)
  }

  render() {
    const {changeTimeseriesFilter, changeTimeseriesFilterValue, selectedFilters, filterOptions} = this.props
    const options = (filterOptions) ? filterOptions.toJS() : []
    const value = (selectedFilters) ? selectedFilters.toJS() : []

    return (
      <div className="time-series-filter">
        <Select
          multi
          options={options}
          optionRenderer={this.renderOption}
          onInputChange={changeTimeseriesFilterValue}
          onChange={changeTimeseriesFilter}
          value={value}
          valueRenderer={this.renderValue}
          filterOptions={this.filterOptions}
        />
      </div>
    )
  }
}

TimeSeriesFilter.propTypes = {
  changeTimeseriesFilter: PropTypes.func.isRequired,
  changeTimeseriesFilterValue: PropTypes.func.isRequired,
  editedTimeSeries: PropTypes.object.isRequired,
  filterValue: PropTypes.string.isRequired,
  filterOptions: PropTypes.object.isRequired,
  selectedFilters: PropTypes.object.isRequired
}
