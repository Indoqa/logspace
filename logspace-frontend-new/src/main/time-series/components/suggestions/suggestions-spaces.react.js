/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'

export default class SuggestionSpaces extends React.Component {

  render() {
    const {selectedSpace, spaces, selectSpace, clearSpace} = this.props

    if (selectedSpace !== null) {
      return (
        <div>
          <ul>
              <li onClick={() => clearSpace()}> {selectedSpace.get('name')} (x) </li>
           </ul>
        </div>
      )
    }

    return (
      <div>
        <ul>
          {spaces.map((item, index) => {
            return <li key={index} onClick={() => selectSpace(item)}> {item.get('name')} </li>
          })}
        </ul>
      </div>
    )
  }
}

SuggestionSpaces.propTypes = {
  selectedSpace: PropTypes.bool.isRequired,
  spaces: PropTypes.array.isRequired,
  selectSpace: PropTypes.func.isRequired,
  clearSpace: PropTypes.func.isRequired
}
