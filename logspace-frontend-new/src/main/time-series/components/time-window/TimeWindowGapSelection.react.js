/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React, {PropTypes} from 'react'
import Immutable from 'immutable'
import {units} from '../../actions/timeWindow.constants'

export default class TimeWindowGapSelection extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      localState: Immutable.fromJS({
        gap: props.value
      })
    }
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      localState: this.state.localState.merge({
        gap: {
          amount: nextProps.value.get('amount'),
          unit: nextProps.value.get('unit')
        }
      })
    })
  }

  onAmountChange(amount) {
    if (isNaN(amount) || amount < 0) {
      return
    }

    const newState = Immutable.fromJS({
      gap: {
        amount,
        unit: this.state.localState.get('gap').get('unit')
      }
    })

    this.setState({
      localState: newState
    })

    this.props.onChange(newState.get('gap'))
  }

  onUnitChange(id) {
    const selectedUnit = units.find((obj) => `${obj.get('id')}` === `${id}`)

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
      <span className="gapselection">
        <input
          size="3"
          type="number"
          pattern="[0-9]{1,3}"
          value={this.state.localState.get('gap').get('amount')}
          onChange={(event) => this.onAmountChange(event.target.value)}
        />
        <select
          name="gap"
          value={this.state.localState.get('gap').get('unit').get('id')}
          onChange={(event) => this.onUnitChange(event.target.value)}
        >
          {units.map((unit) => {
            return <option value={unit.get('id')}>{unit.get('label')}</option>
          })}
        </select>
      </span>
    )
  }
}

TimeWindowGapSelection.propTypes = {
  value: PropTypes.object.isRequired,
  onChange: PropTypes.func.isRequired,
}
