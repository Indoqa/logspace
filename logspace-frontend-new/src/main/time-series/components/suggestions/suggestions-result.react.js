/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import Halogen from 'halogen'

import SuggestionSystems from './suggestions-systems.react'
import SuggestionSpaces from './suggestions-spaces.react'
import SuggestionProperties from './suggestions-properties.react'
import TimeSeriesLabel from '../time-series/time-series-label.react'

export default class SuggestionResult extends React.Component {

  render() {
    const {request, result, addTimeseries} = this.props

    const isLoading = result.get('loading')

    if (isLoading) {
      return (
        <div className={'result'}>
          <div className={'loading'}>
            <span> <Halogen.PulseLoader color={'#ddfcff'} /> </span>
          </div>
        </div>
      )
    }

    const systems = result.get('systems')
    const spaces = result.get('spaces')
    const properties = result.get('propertyNames')

    return (
      <div>
        <div className={'result'}>
          <ul>
            {this.props.result.get('agentDescriptions').map((item, index) => {
              const agent = item.toJS()

              return (
                <li key={index}>
                  <a onClick={() => addTimeseries(agent)}>
                    <div className="color"></div>
                    <div className="inner">
                     <TimeSeriesLabel timeSeries={item} />
                     <div className={'properties'}>
                        {agent.propertyDescriptions.map((description, descriptionIndex) => {
                          const separator = agent.propertyDescriptions.length - 1 > descriptionIndex ? '|' : ''
                          return <span key={description.name}> {description.name} {separator}</span>
                        })}
                      </div>
                    </div>
                  </a>
                </li>
              )
            })}
          </ul>
        </div>

        <div className={'facets'}>
          <b>Spaces</b>
          <SuggestionSpaces spaces={spaces} selected={request.get('space')} />
          <br />
          <b>Systems</b>
          <SuggestionSystems systems={systems} selected={request.get('system')} />
          <br />
          <b>Properties</b>
          <SuggestionProperties properties={properties} selected={request.get('property')} />
        </div>
      </div>
    )
  }
}

SuggestionResult.propTypes = {
  request: PropTypes.object.isRequired,
  result: PropTypes.object.isRequired,
  addTimeseries: PropTypes.func.isRequired
}
