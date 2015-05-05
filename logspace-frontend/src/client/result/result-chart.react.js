/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react';
import PureComponent from '../components/purecomponent.react';
import {onResultRefreshed} from './actions';

export default class Chart extends PureComponent {
  render() {
    const result = this.props.result
    
    if (result.get("error") == true) {
       return (
          <div>
            Logspace REST Error {result.get('errorStatus')}: {result.get('errorText')}
            <button onClick={onResultRefreshed}>Try again</button>
          </div>
        )
    }

    if (result.get("empty") == true) {
       return <div>Please add a time series</div>
    }

    return (
      <div>
        Chart: 
        {result.get("series").map(function(item) {
          var bgStyle = {
            backgroundColor: item.get("color"),
            padding: '5px',
            marginLeft: '5px',
            width: '200px'
          }
          
          return (
            <div key={item.get("id")} style={bgStyle}>
              {item.get("id")}: {item.get("data")} 
            </div>
            );
        })}
      </div>
    );
  }
}