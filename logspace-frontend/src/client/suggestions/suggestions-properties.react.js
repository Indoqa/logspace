/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react';
import PureComponent from '../components/purecomponent.react';
import {onPropertySelected, onPropertyCleared} from './actions';

export default class SuggestionProperties extends PureComponent {
  
  render() {
    if (this.props.selected != null) {
      return (
        <div>
          <ul>
              <li> {this.props.selected} (<a onClick={() => onPropertyCleared()}>x</a>) </li>
           </ul> 
        </div>
      );
    }

    return (
      <div>
        <ul>
          {this.props.properties.map(function(item, index) {
            return <li key={index}> <a onClick={() => onPropertySelected(item)}>{item}</a> </li>;
          })}    
        </ul>
      </div>
    );
  }
}