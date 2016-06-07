/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React, {PropTypes} from 'react'
import SuggestionResult from './SuggestionsResult.react'

import debounceFunc from '../../../app/utils/debounce'

require('./Suggestions.styl')

export default class Suggestions extends React.Component {

  constructor(props) {
    super(props)

    this.state = {
      input: props.suggestions.get('request').get('text')
    }
  }

  componentDidMount() {
    const {setSuggestionQuery} = this.props

    this.debouncedChangeFunction = debounceFunc(() => setSuggestionQuery(this.state.input), 350)
  }

  handleQueryChange(event) {
    this.setState({input: event.target.value})
    this.debouncedChangeFunction()
  }

  render() {
    return (
      <div className={'suggestions'}>
        <div className={'query'}>
            <input
              onChange={(event) => this.handleQueryChange(event)}
              value={this.state.input}
              placeholder="Filter by agent, space, system or property name"
            />
        </div>
        <SuggestionResult
          result={this.props.suggestions.get('result')}
          request={this.props.suggestions.get('request')}
          addTimeseries={this.props.addTimeseries}
          selectSystem={this.props.selectSystem}
          clearSystem={this.props.clearSystem}
          selectProperty={this.props.selectProperty}
          clearProperty={this.props.clearProperty}
          selectSpace={this.props.selectSpace}
          clearSpace={this.props.clearSpace}
        />
      </div>
    )
  }
}

Suggestions.propTypes = {
  suggestions: PropTypes.object.isRequired,
  addTimeseries: PropTypes.func.isRequired,
  setSuggestionQuery: PropTypes.func.isRequired,
  selectSystem: PropTypes.func.isRequired,
  clearSystem: PropTypes.func.isRequired,
  selectProperty: PropTypes.func.isRequired,
  clearProperty: PropTypes.func.isRequired,
  selectSpace: PropTypes.func.isRequired,
  clearSpace: PropTypes.func.isRequired
}
