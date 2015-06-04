/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react';
import Component from '../components/component.react';
import SuggestionResult from './suggestions-result.react';
import {onNewSuggestionQuery} from './actions';

import debounceFunc from '../../lib/debounce'

require('./suggestions.styl')

export default class Suggestions extends Component {

  constructor(props) {
    super(props);

    this.state = {
      input: props.suggestions.get("request").get("text")
    };
  }

  componentDidMount() {
    var me = this;

    this.debouncedChangeFunction = debounceFunc(function() {
      onNewSuggestionQuery(me.state.input)
    }, 350)
  }

  handleQueryChange(event) {
    this.setState({input: event.target.value});
    this.debouncedChangeFunction() 
  }

  render() {
    return (
      <div className={'suggestions'}>
        <div className={'query'}>
           <input onChange={this.handleQueryChange.bind(this)} value={this.state.input} placeholder="Filter by agent, space, system or property name"/>
        </div>
        <SuggestionResult 
          result={this.props.suggestions.get("result")} 
          request={this.props.suggestions.get("request")}
        />
      </div>
    );
  }
}