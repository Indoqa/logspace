/*
 * Logspace
 * Copyright (c) 2015 Indoqa Software Design und Beratung GmbH. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License Version 1.0, which accompanies this distribution and
 * is available at http://www.eclipse.org/legal/epl-v10.html.
 */

import React from 'react'
import PureComponent from '../components/purecomponent.react'

require('./header.styl')

export default class Header extends PureComponent {

  render() {
    return (
      <div className='header'>
        <div className="close"> <input type="button" value="..." onClick={() => onCloseDrawer()}/> </div>
        <div className='logo'>logspace.io</div>
      </div>
    )
  }

}
