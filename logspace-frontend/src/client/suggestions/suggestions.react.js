/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react';
import PureComponent from '../components/purecomponent.react';
import {onNewSuggestionQuery} from './actions';

export default class Suggestions extends PureComponent {
  
  handleQueryChange(event) {
      onNewSuggestionQuery(event.target.value)
  }
  
  render() {
    return (
      <div>
        <input onChange={this.handleQueryChange.bind(this)} /> <br/>
        spaces: 
        <ul>
          {this.props.suggestions.get('spaces').map(function(item, index) {
            return <li key={index}> {item} </li>;
          })}
        </ul>
         agents: 
        <ul>
          {this.props.suggestions.get('agentIds').map(function(item, index) {
            return <li key={index}> {item} </li>;
          })}
        </ul>
         properties: 
        <ul>
          {this.props.suggestions.get('propertyNames').map(function(item, index) {
            return <li key={index}> {item} </li>;
          })}
        </ul>
      </div>
    );
  }
}