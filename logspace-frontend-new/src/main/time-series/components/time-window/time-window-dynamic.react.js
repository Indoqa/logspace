/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React, {PropTypes} from 'react'
import Immutable from 'immutable'

import GapSelection from './time-window-gapselection.react.js'

require('./time-window.styl')

export default class TimeWindowDynamic extends React.Component {

  constructor(props) {
    super(props)

    this.state = {
      localState: Immutable.fromJS({
        range: props.dynamic.get('range'),
        gap: props.dynamic.get('gap')
      })
    }
  }

  onRangeChange(value) {
    const oldAmount = this.state.localState.get('range').get('amount')
    const amountChanged = oldAmount !== value.get('amount')

    if (amountChanged) {
      this.setState({
        localState: this.state.localState.merge({
          range: {
            amount: value.get('amount'),
            unit: value.get('unit')
          }
        })
      })
    } else {
      this.setState({
        localState: this.state.localState.merge({
          range: {
            amount: value.get('amount'),
            unit: value.get('unit')
          },
          gap: {
            amount: 1,
            unit: value.get('unit')
          }
        })
      }) 
    }
  }

  onGapChange(value) {
    this.setState({
      localState: this.state.localState.merge({
        gap: value
      })
    })
  }

  submit() {
    const state = this.state.localState.toJS()

    if (state.range.amount < 1 || state.gap.amount < 1) {
      return
    }

    this.props.selectDynamicDate(state.range.amount, state.range.unit, state.gap)
  }

  render() {
    return (
      <div>
         <div className="selection">
          <div className="submit">
             <button className="waves-effect waves-light btn btn-small" onClick={() => this.submit()}>
              Apply
             </button>
          </div>
          <div className="dynamic">
            <span className="intro">last </span>
            <GapSelection value={this.state.localState.get('range')} onChange={(event) => this.onRangeChange(event)} />
            <span className="intro">Gap</span>
            <GapSelection value={this.state.localState.get('gap')} onChange={(event) => this.onGapChange(event)} />
          </div>
        </div>
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
      </div>
    )
  }
}

TimeWindowDynamic.propTypes = {
  dynamic: PropTypes.object.isRequired,
  selectDynamicDate: PropTypes.func.isRequired
}

