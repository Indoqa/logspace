/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react'
import Component from '../components/component.react'
import Immutable from 'immutable'


import {units} from './constants'

export default class GapSelection extends Component {
  constructor(props) {
    super(props);

    console.log('aaaaaaa')

    this.state = { 
      localState: Immutable.fromJS({
        gap: props.value
      }) 
    }
  }

  onAmountChange(amount) {
    if (isNaN(amount)) {
      return
    } 

    const newState = Immutable.fromJS({
        gap: {
          amount: amount,
          unit: this.state.localState.get('gap').get('unit')
        }
      }) 

    this.setState({
      localState: newState
    })

    this.props.onChange(newState.get('gap'))
  }

  onUnitChange(id) {
    var selectedUnit = units.find(function(obj){return obj.get('id') == id })
    
    const newState = Immutable.fromJS({
        gap: {
          amount: this.state.localState.get('gap').get('amount'),
          unit: selectedUnit
        }
      }) 

    this.setState({
      localState: newState
    })

    this.props.onChange(newState.get('gap'))
  }

  render() {
    return (
      <span>
        <input 
          size='3'
          value={this.state.localState.get('gap').get('amount')} 
          onChange={(event) => this.onAmountChange(event.target.value)} 
        />
        <select 
          name="gap" 
          value={this.state.localState.get('gap').get('unit').get('id')} 
          onChange={(event) => this.onUnitChange(event.target.value)}>
          {units.map(function(unit) {
            return <option value={unit.get('id')}>{unit.get('label')}</option>
          })}
        </select>
      </span>    
    )
  }

}
