/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'

export default class SuggestionSystems extends React.Component {

  render() {
    const {selectedSystem, systems, selectSystem, clearSystem} = this.props

    if (selectedSystem !== null) {
      return (
        <div>
          <ul>
              <li onClick={() => clearSystem()}> {selectedSystem.get('name')} (x) </li>
           </ul>
        </div>
      )
    }

    return (
      <div>
        <ul>
          {systems.map((item, index) => {
            return <li key={index} onClick={() => selectSystem(item)}> {item.get('name')} </li>
          })}
        </ul>
      </div>
    )
  }
}

SuggestionSystems.propTypes = {
  selectedSystem: PropTypes.bool.isRequired,
  systems: PropTypes.array.isRequired,
  selectSystem: PropTypes.func.isRequired,
  clearSystem: PropTypes.func.isRequired
}
