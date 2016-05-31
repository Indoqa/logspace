/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'

export default class SuggestionProperties extends React.Component {

  render() {
    const {selectedProperty, properties, selectProperty, clearProperty} = this.props

    if (selectedProperty) {
      return (
        <div>
          <ul>
              <li onClick={() => clearProperty()}> {selectedProperty.get('name')} (x) </li>
           </ul>
        </div>
      )
    }

    return (
      <div>
        <ul>
          {properties.map((item, index) => {
            return <li key={index} onClick={() => selectProperty(item)}> {item.get('name')} </li>
          })}
        </ul>
      </div>
    )
  }
}

SuggestionProperties.propTypes = {
  selectedProperty: PropTypes.object,
  properties: PropTypes.object.isRequired,
  selectProperty: PropTypes.func.isRequired,
  clearProperty: PropTypes.func.isRequired
}
