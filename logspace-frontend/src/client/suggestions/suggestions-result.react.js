/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react';
import PureComponent from '../components/purecomponent.react';
import SuggestionSystems from './suggestions-systems.react';
import SuggestionSpaces from './suggestions-spaces.react';
import SuggestionProperties from './suggestions-properties.react';
import {onNewTimeSeries} from '../time-series/actions';

export default class SuggestionResult extends PureComponent {

 render() {
    var isLoading = this.props.result.get("loading");

    if (isLoading) {
      return  <div>  <div className={'facets'}> loading </div> </div>
    }

    var request = this.props.request;
    var systems = this.props.result.get("systems");
    var spaces = this.props.result.get("spaces");
    var properties = this.props.result.get("propertyNames");

    return (
      <div>
        <div className={'result'}>
          <ul>
           {this.props.result.get('agentDescriptions').map(function(item, index) {
              var agent = item.toJS();
              return (
                <li key={index}>
                  <a onClick={() => onNewTimeSeries(agent)}> 
                    {item.get('name')} <br/>
                    {item.get('space')};{item.get('system')} 
                  </a>
                </li>
              );
            })}
          </ul>
        </div>
        <div className={'facets'}>
          <b>spaces:</b>
          <SuggestionSpaces spaces={spaces} selected={request.get('space')}/>
          <br/>
          <b>systems:</b>
          <SuggestionSystems systems={systems} selected={request.get('system')}/>
          <br/>
          <b>properties</b>
          <SuggestionProperties properties={properties} selected={request.get('property')}/>
        </div>
      </div>
    );
  }
}