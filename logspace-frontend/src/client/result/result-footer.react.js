/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */
import React from 'react'
import classnames from 'classnames'

import Component from '../components/component.react'

require ('./result-footer.styl')

export default class Footer extends Component {

    getLastUpdated() {
      const lastUpdated = this.props.result.get('lastUpdated')
      
      if(!lastUpdated) {
        return '-'
      }

      return lastUpdated.format('HH:mm:ss')
    }

    render() {
      const lastUpdated = this.getLastUpdated()

      return (
        <div className='footer'>
          Last updated: {lastUpdated}
        </div>
      )
  }
}
