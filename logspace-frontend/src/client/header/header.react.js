/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react'
import Component from '../components/component.react'
import {onShowOptions} from '../options/actions'

require('./header.styl')

export default class Header extends Component {

  render() {
    return (
      <div className='header'>
        <div className="options">
          <span onClick={() => onShowOptions()}
                dangerouslySetInnerHTML={{__html: '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M2 15.5v2h20v-2H2zm0-5v2h20v-2H2zm0-5v2h20v-2H2z"/></svg>'}} />
        </div>
        <div className='logo'>logspace.io</div>
      </div>
    )
  }

}
