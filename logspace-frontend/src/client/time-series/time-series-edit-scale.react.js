/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react';
import Component from '../components/component.react';

import {default as LiteDropdown} from 'react-lite-dropdown';
import 'react-lite-dropdown/src/style.css'; 

import {onTimeSeriesPropertyChanged} from './actions';
import {cleanPropertyName} from './time-series-item.react'
import {COLORS, TYPES, SCALES, isSubitem, getReference} from './constants';

require('./time-series-edit-scale.styl')

export default class EditTimeSeriesScale extends Component {

  constructor(props) {
    super(props)
    
    this.state = {
      scaleTypeDropdownShown: false
    }
  }

  handleChange(event) {
    onTimeSeriesPropertyChanged(event.target.name, event.target.value)
  }

  toggleScaleTypeDropdownShown() {
    const currentValue = this.state.scaleTypeDropdownShown 
    this.setState({ scaleTypeDropdownShown: !currentValue})
  }

  getReferenceHTML(item) {
    const styles = {borderLeft: '6px solid ' + item.get('color')}
    return (
      <div className='time-series-label' style={styles}>
        <span className={'meta'}>
          <span className='space'>{item.get('space')}</span>
          <span className='system'>{item.get('system')}</span>
        </span>
        <span>{item.get('name')}: </span>
        <span className='property'>{item.get("aggregate")} of {cleanPropertyName(item.get("propertyId"))}</span>
      </div>
    )
  }

  getCurrentSelectedScaleHTML(scaleType) {
    if (this.state.scaleTypeDropdownShown) {
      return <div className='manual'><i> - Apply to existing or create a new scale - </i></div>
    }

    if (scaleType === 'auto') {
      return <div className='manual'> Data (use min/max of result) </div>
    }

    if (scaleType === 'custom') {
      return <div className='manual'> Custom range </div>
    }

    if (scaleType === 'property') {
      const propertyScale = SCALES[this.props.editedTimeSeries.get("newItem").get("propertyId")]
      return <div className='manual'> {propertyScale.label} </div>
    }

    if (isSubitem(scaleType)) {
      const item = this.props.timeSeries.find(function(item) {return item.get('id')  === getReference(scaleType) })
      return this.getReferenceHTML(item)
    }


    return <div> {scaleType}</div>
  }

  getCustomScaleInput(agentDescription) {
    if (agentDescription.get("scaleType") != 'custom') {
      return <div/>
    }

    return (<span>
      Range 
      <nbsp/>  <nbsp/>
      <input name='scaleMin'
        value={agentDescription.get("scaleMin")}
        onChange={this.handleChange.bind(this)}/>
      <nbsp/> - <nbsp/>
      <input name='scaleMax'
        value={agentDescription.get("scaleMax")}
        onChange={this.handleChange.bind(this)}/>
      </span>)
  }

   getPropertyScaleInput(agentDescription) {
    const propertyScale = SCALES[agentDescription.get("propertyId")]

    if (propertyScale == null) {
      return <div/>
    }

    const savePropertyScale = () => {
      onTimeSeriesPropertyChanged('scaleType', 'property')
      onTimeSeriesPropertyChanged('scaleMin', propertyScale.min)
      onTimeSeriesPropertyChanged('scaleMax', propertyScale.max)
    }

    return (<div key={'property'} className={'item'} onClick={savePropertyScale}>
        <div className='manual'>{propertyScale.label} ({propertyScale.min} - {propertyScale.max})</div>
      </div>)
  }

  getCustomDataInput() {
    return (
      <div key={'data'} className={'item'} onClick={() => onTimeSeriesPropertyChanged('scaleType', 'auto')}>
        <div className='manual'> Data (use min/max of result) </div>
      </div>
    )
  }

  getCustomRangeInput() {
    return (
      <div key={'custom'} className={'item'} onClick={() => onTimeSeriesPropertyChanged('scaleType', 'custom')}>
        <div className='manual'>  Custom range </div>
      </div>
    )
  }

  getExistingScaleInput() {
    var me = this
    let found = false

    const items = this.props.timeSeries.map(function(item) {
      if (item.get('id') === me.props.editedTimeSeries.get("newItem").get('id')) {
        return null
      }

      if (isSubitem(item.get('scaleType'))) {
        return null
      }

      found = true

      const key = 'subitem-' + item.get('id')
      const styles = {borderLeft: '6px solid ' + item.get('color'), backgroundColor: '#F7F7F7'}
      const referenceHTML = me.getReferenceHTML(item)

      return (<div key={key} className={'item'} onClick={() => onTimeSeriesPropertyChanged('scaleType', key)}> 
        {referenceHTML}
      </div>)
    })

    if (found) {
      return (<div>
        <div className='separator'> Apply to an existing scale</div>
        {items}
      </div>) 
    }

    return <div/>
  }

  render() {
    const me = this
    const agentDescription = this.props.editedTimeSeries.get("newItem");
  
    const existingScaleInput = this.getExistingScaleInput()
    const propertyScaleInput = this.getPropertyScaleInput(agentDescription)
    const customScaleInput = this.getCustomScaleInput(agentDescription)
    const customRangeInput = this.getCustomRangeInput()
    const customDataInput = this.getCustomDataInput()
    
    const selectedScaleTypeLabel = this.getCurrentSelectedScaleHTML(agentDescription.get("scaleType"))

    return (
      <div>    
        <b>Select Scale</b>
        <LiteDropdown
          displayText={selectedScaleTypeLabel}
          defaultText={'not used'}
          show={this.state.scaleTypeDropdownShown}
          onToggle={() => this.toggleScaleTypeDropdownShown()}
          name={'css-hook-demo'}>
            {existingScaleInput}
            <div className='separator'> Create a new Scale </div>
            {customDataInput}
            {propertyScaleInput}
            {customRangeInput}
        </LiteDropdown>
        <br/>
        {customScaleInput}
      </div>
    );
  }

}
