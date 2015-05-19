/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react';
import PureComponent from '../components/purecomponent.react';
import SuggestionResult from './suggestions-result.react';
import {onNewSuggestionQuery} from './actions';
import {onCloseDrawer} from '../drawer/actions';

require('./suggestions.styl')

export default class Suggestions extends PureComponent {
  
  handleQueryChange(event) {
      onNewSuggestionQuery(event.target.value)
  }

  render() {
    return (
      <div className={'suggestions'}>
        <div className={'query'}>
           <input onChange={this.handleQueryChange.bind(this)} value={this.props.suggestions.get("request").get("text")}/>
           <input type="button" value="cancel" onClick={() => onCloseDrawer()}/> 
        </div>
        <SuggestionResult 
          result={this.props.suggestions.get("result")} 
          request={this.props.suggestions.get("request")}
        />
      </div>
    );
  }
}